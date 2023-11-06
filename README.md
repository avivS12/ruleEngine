
# RuleEngine

RuleEngine class enables the user to check events received from a message broker against a set of user defined rules, and raise an alert in case an event complies with a rule. Every event is logged in a Mongodb database. 

## Technologies
    RabbitMQ
    Mongodb
    Java


## How to Use RuleEngine class

1. Ctor :
     
   signature: ```public RuleEngine(String amqpUrlAlert, String queueNameAlert, String amqpUrl, String queueName, String dbUri, String dbName) throws IOException, TimeoutException```
   
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

     
2. Run RuleEngine
   signature: ```public void run()```
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


3. Shutdown RuleEngine
   signature: ```public void shutDown()```
    when user wishes to shutdown the RuleEngine he can call the shutdown method.

    Example:
        ruleEngine.shutdown();
    
    
4. Add new rule
   In order to add new rule, the user should open 'rules.csv' and add new rule to it by this order:
   rule-id(int unique id)
   vehicle-type(String for example drone)
   check-field(String field of event which contains the target value to check/ for example "cpu". In case of a logic rule this field should contain the key word "logic").
   operator(String "greater"/"less"/"greater or equals"/"less or equals"/"equals") in case of logical rule the field should contain "and"/"or"
   threshold(the value which is the threashold for the rule)
   priority(String value between 1 - 3, specifies the ergency of the event)
   message(String the message that would be sent to the alert micro service)
   is-time-constrain (currently unavailable, value should be "false". This field is for future infrastructure to write rules with time constrain)
   time-duration-seconds(currently unavailable)
   
   Enables the user to define new rules.
    The method takes as parameters String ruleName and Rule newRule.

    Every Rule has to implement the Rule interface:
        interface Rule {
            boolean match(JSONObject data);
            String message();
        }

    Example:
       1, drone, cpu, greater, 80, 1, cpu over 80, false, 0
       2, drone, battery, less, 30, 1, battery less then 30 percent, false, 0
       3, drone, long, greater, 32.08545236664342, 1, longtitude over 32.08545236664342, false, 0
       4, drone, lat, greater, 34.766925459287044, 1, latitude over 34.766925459287044, false, 0
       5, drone, logic, and ,3 - 4, 1, combination for rules 3 + 4 location, false, 0

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

