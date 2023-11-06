package org.example;

import com.rabbitmq.client.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class RabbitReceiver implements Runnable{
    private String queueName = null;
    private Consumer<JSONObject> handler = null;
    private ConnectionFactory factory = new ConnectionFactory();
    private Connection connection = null;
    private Channel channel = null;

    public RabbitReceiver(Consumer<JSONObject> handler, String queueName, String amqpUrl, int priority) throws IOException, TimeoutException {
        this.handler = handler;
        this.queueName = queueName; //"AlertPQ"
        Map<String, Object> queueArgs = new HashMap<>();
        queueArgs.put("x-max-priority", priority);
        try {
            this.connection = factory.newConnection(amqpUrl);
            this.channel = connection.createChannel();
            channel.queueDeclare(queueName, true, false, false, queueArgs);
        } catch (Exception e) {
            if (channel != null) {
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
            throw new IOException(e);
        }
    }

    public RabbitReceiver(Consumer<JSONObject> handler, String queueName, String amqpUrl) throws IOException, TimeoutException {
        this(handler, queueName, amqpUrl, 3);
    }

    public void run() {

        DeliverCallback deliverCallback = (s, delivery) -> {
            JSONObject event = new JSONObject(new String(delivery.getBody(), "UTF-8"));
            handler.accept(event);
        };

        CancelCallback cancelCallback = s -> {
            System.out.println(s);
        };
        try {
            channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutDown() {
        try {
            this.connection.close();
        }catch (IOException e) {
            System.out.println("exception in connection close");
        }
    }
}
