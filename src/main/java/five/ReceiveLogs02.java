package five;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import utils.RabbitMqUtils;

//消息的接收
public class ReceiveLogs02
{
    public final static String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws Exception
    {
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机 第一个参数是交换机的名字 第二个参数是交换机的类型
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        //声明一个临时队列  消费者断开与队列连接时 队列自动删除
        // 进行绑定
        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue,EXCHANGE_NAME,"");

        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("交换机2接收到了消息："+new String(message.getBody(),"UTF-8"));
        };


        channel.basicConsume(queue,true,deliverCallback,consumerTag -> {});
        System.out.println("正在等待消息进入......");
    }
}
