package seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import utils.RabbitMqUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 主题交换机
 * 生产者
 */
public class TopicLogs
{
    public final static String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception
    {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        Scanner scanner = new Scanner(System.in);
        String message = scanner.next();
        channel.basicPublish(EXCHANGE_NAME,"lazy.eee.zz",null,message.getBytes(StandardCharsets.UTF_8));
        System.out.println("发送了"+message);
    }
}
