package org.example;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*; //Map, HashMap, Collection
import java.io.FileReader;

public class RuleManager {
    private final int DEFAULT_PRIORITY = 3;
    Map<Integer, Rule> rules = new HashMap<>();
    RabbitSender alert = null;

    public RuleManager(RabbitSender alert) throws IOException {
        this.alert = alert;
        try (BufferedReader br = new BufferedReader(new FileReader("rules.csv"))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Rule rule = new RuleImp(values);
                addRule(values[1], Integer.parseInt(values[0]), rule);
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
        System.out.println("size of rules map" + rules.size());
    }

    public void addRule(String vehicleType, Integer ruleId, Rule newRule) {
        rules.put(ruleId, newRule);
    }

    public void removeRule(String name) {
        rules.remove(name);
    }

    public void checkEvent(JSONObject event) {

        Collection<Rule> ruleSet = rules.values();
        for(Rule rule: ruleSet) {
            if (rule.match(event)) {
                handleEvent(rule, DEFAULT_PRIORITY);
                break;
            }
        }
    }

    public void shutdown() {
        this.alert.shutdown();
    }

    private void handleEvent(Rule rule, int priority) {
        alert.accept(rule.message(), priority);
    }

    private class RuleImp implements Rule {
        private int ruleId = 0;
        private String ruleVehicleType = null;
        private String ruleFieldToCheck = null;
        private String ruleThreshold = null;
        private String ruleOperator = null;

        RuleImp(String[] ruleLine) {
            ruleId = Integer.parseInt(ruleLine[0]);
            ruleVehicleType = ruleLine[1];
            ruleFieldToCheck = ruleLine[2];
            ruleOperator = ruleLine[3];
            ruleThreshold = ruleLine[4];

        }

        @Override
        public boolean match(JSONObject data) {
            boolean ans = false;
            try {
                String type = (String)data.get("vehicle-type");
                String dataValue = String.valueOf(data.get(ruleFieldToCheck));

                if (dataValue.equals("logic")) {

                    if (type.equals(ruleVehicleType) && evaluateLogic(dataValue, data)) {
                        ans = true;
                    }
                } else {
                    if (type.equals(ruleVehicleType) && evaluate(dataValue)) {
                        ans = true;
                    }
                }
            }catch(JSONException ignored){}
            return ans;
        }

        @Override
        public String message() {
            StringBuilder ans = new StringBuilder("{ruleVehicleType:" +ruleVehicleType);
            ans.append(", ruleFieldToCheck:").append(ruleFieldToCheck);
            ans.append(", ruleOperator:").append(ruleOperator);
            ans.append(", then: ").append(ruleThreshold);
            ans.append("}");
            return new String(ans);
        }

        private boolean evaluate(String value) {
            switch (ruleOperator) {
                case ("greater"):
                    return (Double.parseDouble(value) > Double.parseDouble(ruleThreshold));
                case("less"):
                    return (Double.parseDouble(value) < Double.parseDouble(ruleThreshold));
                case ("greater or equals"):
                    return (Double.parseDouble(value) >= Double.parseDouble(ruleThreshold));
                case ("less or equals"):
                    return (Double.parseDouble(value) <= Double.parseDouble(ruleThreshold));
                case ("equals"):
                    return (Double.parseDouble(value) == Double.parseDouble(ruleThreshold));
                
            }
            return false;
        }

        private boolean evaluateLogic(String value, JSONObject data) {
            String[] rulesIdArray = ruleThreshold.split("-");
            Rule rule1 = rules.get(Integer.parseInt(rulesIdArray[0]));
            Rule rule2 = rules.get(Integer.parseInt(rulesIdArray[1]));

            switch(value) {
                case ("and"):
                    return (rule1.match(data) && rule2.match(data));
                case ("or"):
                    return (rule1.match(data) || rule2.match(data));
            }
            return false;
        }
    }
}
