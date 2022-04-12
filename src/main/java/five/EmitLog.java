package five;

import com.rabbitmq.client.Channel;
import utils.RabbitMqUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

//发送消息
public class EmitLog
{
    public final static String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception
    {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        Scanner sc = new Scanner(System.in);
        while (sc.hasNext())
        {
            String message = sc.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送消息："+message);
        }
    }
}
