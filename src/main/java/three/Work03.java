package three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import utils.RabbitMqUtils;
import utils.SleepUtils;

public class Work03
{
    private final static String QUEUE_NAME = "ack_test";
    public static void main(String[] args) throws Exception
    {
        Channel channel = RabbitMqUtils.getChannel();
        DeliverCallback deliverCallback = (var1, var2) ->
        {
            SleepUtils.sleep(1);
            System.out.println("接收到信息:"+new String(var2.getBody(),"UTF-8"));  //设置编码 以防乱码
            //手动应答  获得消息标记
            channel.basicAck(var2.getEnvelope().getDeliveryTag(),false);
        };

        //预取值的设置
        channel.basicQos(2);

        System.out.println("C1等待消息接收......反应时间较快");
        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费成功后的回调
         * 4.消费者未成功消费的回调
         */
        Boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME,autoAck,deliverCallback,(tag)->{
            System.out.println("接收消息失败");
        });
    }
}
