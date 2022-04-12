package eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

public class Consumer01
{
    public final static String NORMAL_EXCHANGE = "normal_exchange";
    public final static String DEAD_EXCHANGE = "dead_exchange";
    public final static String NORMAL_QUEUE = "normal_queue";
    public final static String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception
    {
        Channel channel = RabbitMqUtils.getChannel();
        //开始创建 和声明
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //需要设置参数才能再过期或者什么情况下转发到死信队列
        Map<String, Object> arguments = new HashMap<>();
        //正常队列设置死信交换机  当你的被拒绝了 你的东西去往哪
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key","lisi");
        //设置消息最大能有多少条  超出则加入死信队列
        //arguments.put("x-max-length",6);
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);
        //
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");

        DeliverCallback deliverCallback = (String consumerTag, Delivery message)->{
            String msg = new String(message.getBody(),"UTF-8");
            if (msg.equals("输出5"))
            {
                System.out.println(msg+"被拒绝接收");
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else
            {
                System.out.println("正常队列"+msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };

        Boolean autoAck = false; //设置是否自动应答
        channel.basicConsume(NORMAL_QUEUE,autoAck,deliverCallback,consumerTag -> {});
        System.out.println("等待打印");
    }
}
