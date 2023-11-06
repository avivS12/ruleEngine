package org.example;

import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class AlertMicroService {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        RabbitReceiver alert = new RabbitReceiver(new EventHandler(), "AlertQueue", "amqp://guest:guest@localhost:5672/");
        Thread t1 = new Thread(alert);
        t1.start();

        Thread.sleep(5000);

        alert.shutDown();
    }
    private static class EventHandler implements Consumer<JSONObject> {

        @Override
        public void accept(JSONObject event) {
            System.out.println(event);
        }
    }
}
