package org.example;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.ObjIntConsumer;

public class RabbitSender implements ObjIntConsumer<String> {
    private String queueName = null;
    private static final Map<String, Object> queueArgs = new HashMap<>();
    private static ConnectionFactory factory = new ConnectionFactory();
    private Connection connection = null;
    private Channel channel = null;
    private AMQP.BasicProperties.Builder properties = null;

    public RabbitSender(String ampqUrl, String queueName, int priority) throws IOException {
        this.queueName = queueName;
        queueArgs.put("x-max-priority", priority);

        try {
            connection = factory.newConnection(ampqUrl);
            channel = connection.createChannel();
        } catch (IOException | TimeoutException e) {
            assert connection != null;
            connection.close();
            throw new RuntimeException(e);
        }
        channel.queueDeclare(queueName, true, false, false, queueArgs);
        properties = new AMQP.BasicProperties.Builder();
    }

    public void shutdown() {
        try {
            this.channel.close();
        } catch (IOException | TimeoutException ignored) {}
        try {
            this.connection.close();
        }catch (IOException ignored) {}
    }
    @Override
    public void accept(String message, int priorityInt) {
        properties.contentType("text/plain").priority(priorityInt);
        try {
            channel.basicPublish("", queueName, properties.build(), message.getBytes());
        } catch(IOException e) {
            shutdown();
        }
    }
}
