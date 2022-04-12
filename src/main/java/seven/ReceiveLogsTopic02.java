package seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import utils.RabbitMqUtils;

public class ReceiveLogsTopic02
{
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception
    {
        Channel channel = RabbitMqUtils.getChannel();
        //声明队列
        channel.queueDeclare("Q2",false,false,false,null);
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueBind("Q2",EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind("Q2",EXCHANGE_NAME,"lazy.#");

        System.out.println("等待接收消息...");
        DeliverCallback deliverCallback = (String consumerTag, Delivery message)->{
            System.out.println("ReceiveLogsTopic02控制台打印"+new String(message.getBody(),"UTF-8")+"绑定的键是"+message.getEnvelope().getRoutingKey());
        };
        channel.basicConsume("Q2",true,deliverCallback,consumerTag -> {});
    }
}
