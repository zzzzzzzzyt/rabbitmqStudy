package two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import utils.RabbitMqUtils;

/*
    消费线程 用于接收消息
 */
public class Work01
{
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception
    {
        DeliverCallback deliverCallback = (String var1, Delivery var2)->{
            System.out.println(new String(var2.getBody()));
        };

        CancelCallback cancelCallback = var1->{
            System.out.println("消息执行失败");
        };


        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("C2等待消息接收......");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
