package org.example;

import org.json.JSONObject;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class RuleEngine implements Runnable {

    private final int HIGHEST_PRIORITY = 3;
    private RuleManager ruleManager = null;
    private DatabaseHandler dbHandler = null;
    private RabbitReceiver receiver = null;

    public RuleEngine(String amqpUrlAlert, String queueNameAlert, String amqpUrl, String queueName, String dbUri, String dbName) throws IOException, TimeoutException {
        RabbitSender alert = null;
        try {
            alert = new RabbitSender(amqpUrlAlert, queueNameAlert, HIGHEST_PRIORITY);
            this.ruleManager = new RuleManager(alert);
        } catch (IOException e) {
            throw new IOException(e);
        }

        try {
            this.receiver = new RabbitReceiver(new EventHandler(), queueName, amqpUrl, HIGHEST_PRIORITY);
        } catch (IOException e) {
            alert.shutdown();
            throw new IOException(e);
        }

        this.dbHandler = new DatabaseHandler(dbUri, dbName);

    }

    @Override
    public void run() {
        receiver.run();
    }

    public void shutDown() {
        this.dbHandler.close();
        this.ruleManager.shutdown();
        receiver.shutDown();
    }

    public void removeRule(String name) {
        ruleManager.removeRule(name);
    }

    private class EventHandler implements Consumer<JSONObject>{

        @Override
        public void accept(JSONObject event) {
            Set<String> keys = event.keySet();
            ruleManager.checkEvent(event);
            dbHandler.update(event, String.valueOf(event.get("vehicle-id")));
        }
    }

}

