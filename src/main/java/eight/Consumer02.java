package eight;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import utils.RabbitMqUtils;



public class Consumer02
{

    public final static String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception
    {
        Channel channel = RabbitMqUtils.getChannel();


        DeliverCallback deliverCallback = (String consumerTag, Delivery message)->{
            System.out.println("死信队列"+new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,consumerTag -> {});
        System.out.println("等待打印");
    }
}
