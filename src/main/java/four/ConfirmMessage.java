package four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import utils.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

//发布确认
public class ConfirmMessage
{
    public final static int MESSAGE_COUNT = 1000;

    /**
     * 确认发布模式
     * 比较使用的时间 确定哪种方式是最好的
     * @param args
     */
    public static void main(String[] args) throws Exception
    {
        //1.单个确认
        //publishMessageIndividually();//总花费时间642ms
        //2.批量确认
        //publishMessageBatch();//总花费时间161ms
        //3.异步确认
        publishMessageAsync();//总花费时间60ms
    }

    //单个发布确认
    private static void publishMessageIndividually() throws Exception
    {
        Channel channel = RabbitMqUtils.getChannel();
        //队列名的声明
        UUID queueName = UUID.randomUUID();
        //b2是自动删除
        channel.queueDeclare(queueName.toString(),true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //发送一千个数据 每次都确定
        for (int i = 1; i <= MESSAGE_COUNT; i++)
        {
            channel.basicPublish("",queueName.toString(),null,(i+"").getBytes());
            //每次都进行发布确认
            channel.waitForConfirms();
        }
        long end = System.currentTimeMillis();
        System.out.println("总花费时间"+(end-begin)+"ms");
    }

    //批量发布确认
    private static void publishMessageBatch() throws Exception
    {
        Channel channel = RabbitMqUtils.getChannel();
        //队列名的声明
        UUID queueName = UUID.randomUUID();
        //b2是自动删除
        channel.queueDeclare(queueName.toString(),true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        int batch = 100;
        //开始时间
        long begin = System.currentTimeMillis();
        //发送一千个数据 每次都确定
        for (int i = 1; i <= MESSAGE_COUNT; i++)
        {
            channel.basicPublish("",queueName.toString(),null,(i+"").getBytes());
            //每次都进行发布确认
            if(i%batch==0)
            {
                channel.waitForConfirms();
            }

        }
        long end = System.currentTimeMillis();
        System.out.println("总花费时间"+(end-begin)+"ms");
    }

    //异步发布确认
    private static void publishMessageAsync() throws Exception
    {

        Channel channel = RabbitMqUtils.getChannel();
        //队列名的声明
        UUID queueName = UUID.randomUUID();
        //b2是自动删除
        channel.queueDeclare(queueName.toString(),true,false,false,null);
        //开启发布确认
        channel.confirmSelect();


        //这个的作用就是为了 把没有发布出去的东西保存下来
        ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();

        //消息确认成功回调函数
        /**
         * 第一个参数 消息的标记
         * 第二个参数 是否批量确认
         */
        ConfirmCallback ackCallback = (deliveryTag,multiple)->{
            //删除已经确认的消息

            if(multiple)
            {
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);  //假如是批量的话 就是批量删除 发一次就是发集群
                confirmed.clear();
            }else
            {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("已确认的消息"+deliveryTag);
        };

        //消息确认失败回调函数
        ConfirmCallback nackCallback = (deliveryTag,multiple)->{
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息："+deliveryTag+"：：：：：：：：未确认消息的标记："+message);
        };


        //开启监听器
        channel.addConfirmListener(ackCallback,nackCallback);

        //开始时间
        long begin = System.currentTimeMillis();
        //发送一千个数据 每次都确定
        for (int i = 1; i <= MESSAGE_COUNT; i++)
        {
            channel.basicPublish("",queueName.toString(),null,(i+"").getBytes());
            //每次进来就确认一次
            outstandingConfirms.put(channel.getNextPublishSeqNo(),i+"");
        }
        long end = System.currentTimeMillis();
        System.out.println("总花费时间"+(end-begin)+"ms");
    }



}
