package org.example;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;


public class Test {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        Consumer<String> alert = event -> System.out.println(event);

        RuleEngine ruleEngine = new RuleEngine("amqp://guest:guest@localhost:5672/","AlertQueue", "amqp://guest:guest@localhost:5672/", "PQ", "mongodb://localhost:27017", "DatabaseTest");
        Thread thread = new Thread(ruleEngine);
        thread.start();

        Thread.sleep(5000);
        ruleEngine.shutDown();
        thread.join();
    }
}
