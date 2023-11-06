
# Event Ingestion Assignment
This project was written as a home assignment. The main purpose of it was to design and program a system that reads evens from message broker, runs them again a set of user defined rules, and in case events matches to a onr of the rules the system alerts about that event. In addition the system should log all the ingested events in a database of my choice.

One of the guidelines were to design the system as a composition of micro-services. In that light i design two different services:
1. RuleEngine - which pulls events from the message broker, checks them against the rules and if necessary sends the event to the alerts queue in the message broker.
2. 

# RuleEngine

RuleEngine class enables the user to check events received from a message broker against a set of user defined rules, and raise an alert in case an event complies with a rule. Every event is logged in a Mongodb database. 

## Technologies
    RabbitMQ
    Mongodb
    Java


## How to Use RuleEngine class

1. Ctor:
    takes as parameters:
    @ data for establishing connection to rabbitmq queue for alert sending, through which the Alert microService will alert events matching    
    the defined rules.
    @ data for establishing connection to rabbitmq queue for receiving events.
    @ data for for establishing connection to MongoDb database.
    
    in case of a failure while establishing all the necessary connections Ctor will 
    throw an IOException.
    
    Example:
            Consumer<String> alert = event -> System.out.println(event);

            RuleEngine ruleEngine = new RuleEngine("amqp://guest:guest@localhost:5672/","AlertQueue", "amqp://guest:guest@localhost:5672/", "PQ", "mongodb://localhost:27017", "DatabaseTest");

     
3. Run RuleEngine
    as soon as the ctor is completed, the RuleEngine object is ready to run. 
    while running the RuleEngine object retreives messages from the message broker, 
    checks them against the defined rules, alerts if necessary and logs them in the  
    database.

    This is a blocking method, which can be run as a seperate thread at the user 
    convenience.
    
    Example:
        ruleEngine.run();
        or
        Thread thread = new Thread(ruleEngine); //runs on a seperate thread
        thread.start();


4. Shutdown RuleEngine
    when user wishes to shutdown the RuleEngine he can call the shutdown method.

    Example:
        ruleEngine.shutdown();
    
    
5. Add new rule
    Enables the user to define new rules.
    The method takes as parameters String ruleName and Rule newRule.

    Every Rule has to implement the Rule interface:
        interface Rule {
            boolean match(JSONObject data);
            String message();
        }

    Example:
        Rule rule1 = new Rule() {
                @Override
                public boolean match(JSONObject data) {
                    boolean ans = false;
                    try {
                        Object type = data.get("vehicle-type");
                        Object cpu = data.get("cpu");

                        if (type.equals("drone") && (Integer)cpu > 80) {
                            ans = true;
                        }
                    }catch(JSONException ignored){}

                    return ans;
                }

                @Override
                public String message() {
                    return "cpu of drone over 80%";
                }
        };

        ruleEngine.addRule("cpuOver80",rule1);

6. Remove rule
    Enables the user to delete an existing rule.
    
    Example:
        ruleEngine.removeRule("cpuOver80");


## Dependencies
    https://mvnrepository.com/artifact/org.json/json
    https://mvnrepository.com/artifact/org.mongodb/mongo-java-driver
    https://mvnrepository.com/artifact/com.rabbitmq/amqp-client
    https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    https://mvnrepository.com/artifact/org.slf4j/slf4j-simple









## Badges

Add badges from somewhere like: [shields.io](https://shields.io/)

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)
[![AGPL License](https://img.shields.io/badge/license-AGPL-blue.svg)](http://www.gnu.org/licenses/agpl-3.0)


## Acknowledgements

 - [Awesome Readme Templates](https://awesomeopensource.com/project/elangosundar/awesome-README-templates)
 - [Awesome README](https://github.com/matiassingers/awesome-readme)
 - [How to write a Good readme](https://bulldogjob.com/news/449-how-to-write-a-good-readme-for-your-github-project)

## Color Reference

| Color             | Hex                                                                |
| ----------------- | ------------------------------------------------------------------ |
| Example Color | ![#0a192f](https://via.placeholder.com/10/0a192f?text=+) #0a192f |
| Example Color | ![#f8f8f8](https://via.placeholder.com/10/f8f8f8?text=+) #f8f8f8 |
| Example Color | ![#00b48a](https://via.placeholder.com/10/00b48a?text=+) #00b48a |
| Example Color | ![#00d1a0](https://via.placeholder.com/10/00b48a?text=+) #00d1a0 |


## Features

- Light/dark mode toggle
- Live previews
- Fullscreen mode
- Cross platform

