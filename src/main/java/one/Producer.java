package one;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Producer
{
    private final static String QUEUE_NAME = "hello"; //设置默认队列名称

    public static void main(String[] args) throws IOException, TimeoutException
    {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂ip连接rabbitmq队列
        factory.setHost("192.168.80.129");  //
        factory.setUsername("admin");
        factory.setPassword("123");

        //获取connection  通过connection 获取信道
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化 默认消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
         * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
         * 5.其他参数
         */

        //设置队列可以有的优先级
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority",10);
        channel.queueDeclare(QUEUE_NAME,false,false,false,arguments);

        /**
         * 发送一个消息
         * 1.发送到那个交换机
         * 2.路由的 key 是哪个
         * 3.其他的参数信息
         * 4.发送消息的消息体
         */
        for (int i = 1; i <= 10; i++)
        {
            String message = "info"+i;
            if (i==5)
            {
                //给该消息设置优先级 不设置 则为默认顺序
                AMQP.BasicProperties props = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("",QUEUE_NAME,props,message.getBytes());
            }else {
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            }
        }

    }
}
