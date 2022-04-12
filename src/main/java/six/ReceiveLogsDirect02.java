package six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import utils.RabbitMqUtils;

public class ReceiveLogsDirect02
{
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception
    {
        Channel channel = RabbitMqUtils.getChannel();
        //声明队列
        channel.queueDeclare("disk",false,false,false,null);
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueBind("disk",EXCHANGE_NAME,"error");

        System.out.println("等待接收消息...");
        DeliverCallback deliverCallback = (String consumerTag, Delivery message)->{
            System.out.println("ReceiveLogsDirect02控制台打印"+new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume("disk",true,deliverCallback,consumerTag -> {});
    }
}
