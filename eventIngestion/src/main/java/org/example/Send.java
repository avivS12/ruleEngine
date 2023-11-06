package org.example;
import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;


public class Send {
    private final static String QUEUE_NAME = "PQ";
    private static final String AMQP_URL = "amqp://guest:guest@localhost:5672/";
    private static final Map<String, Object> queueArgs = new HashMap<>();

    public static void main(String[] args) throws Exception {
        queueArgs.put("x-max-priority", 3);

        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection(AMQP_URL);
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, queueArgs);

        AMQP.BasicProperties.Builder properties = new AMQP.BasicProperties.Builder();

        for (int i = 0; i < 4; i++) {
            int priorityInt = i%3;
           String message = "{vehicle-id:121212,vehicle-type:drone,cpu:100,priority:" + priorityInt +"}";
            properties.contentType("text/plain").priority(priorityInt);
            channel.basicPublish("", QUEUE_NAME, properties.build(), message.getBytes());
        }
        channel.close();
        connection.close();
    }
}
