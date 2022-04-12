package two;

import com.rabbitmq.client.Channel;
import utils.RabbitMqUtils;

import java.util.Scanner;

/*
    生产者 用于发送大量信息
 */
public class Task01
{
    private final static String QUEUE_NAME = "hello";
    public static void main(String[] args) throws Exception
    {
        Channel channel = RabbitMqUtils.getChannel();

        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化 默认消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
         * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext())
        {
            String message = sc.next();
            /**
             * 发送一个消息
             * 1.发送到那个交换机
             * 2.路由的 key 是哪个
             * 3.其他的参数信息
             * 4.发送消息的消息体
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("消息发送完成"+message);
        }
    }
}
