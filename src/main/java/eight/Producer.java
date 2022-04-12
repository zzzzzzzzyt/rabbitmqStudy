package eight;


import com.rabbitmq.client.Channel;
import utils.RabbitMqUtils;

import java.nio.charset.StandardCharsets;

public class Producer
{  public final static String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception
    {
        Channel channel = RabbitMqUtils.getChannel();
        for (int i = 10; i > 0; i--)
        {
            //设置消息过期时间
            //AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
            //        .builder().expiration("10000").build();

            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,("输出"+i).getBytes(StandardCharsets.UTF_8));
        }
    }
}
