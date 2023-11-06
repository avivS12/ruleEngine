# Event Ingestion Assignment
This project was written as a home assignment. The main purpose of it is to design and write a system that reads evens from message broker, runs them again a set of user defined rules, and in case events matches to a onr of the rules the system alerts about that event. In addition the system should log all the ingested events in a database of my choice.

One of the guidelines of this assignment was to design the system as a composition of micro-services. In light of that, i designed two different services:
1. RuleEngine - which pulls events from the message broker, checks them against the rules, and if necessary sends the event to the alerts queue in the message broker.
2. AlertMicroService - pulls events from the alerts queue and carries out some business logic (in this project it prints the rules' message, but it can take any consumer the user wishes to execute).

## System Flow Chart
![image](https://github.com/avivS12/ruleEngine/assets/150012956/5c8b3694-22b1-4a3a-9a5e-dd7ab380a14a)

## Class Diagram
![image](https://github.com/avivS12/ruleEngine/assets/150012956/f951437d-8327-47fd-8eec-3ee48c1e5616)

## Challenges
    Firstly, research and learn new technologies. While writing this project i got to learn and apply new knowledge about micro services, message brokers, and non-sql dbms (mongo db).
    Secondly, i had to design the system, write the classes and define the relations between them, i have tried to write reusable and abstract classes. After that i had the general picture ready (i had basic theoretical understanding, i had the components for my project), and i felt ready to implement it.

    In addition i wanted to enable non software engeneers to be able to write rules. for that purpose i wrote the RuleMeneger class which reads rules from a csv file. To acheive dynamic rule adding ability i wanted to create an observer that will detect changes in the file (such as new lines of rules that were added) and update the rules of the RuleManager auotomatically. I did not had the time to complete that feature. 


## Technologies
    RabbitMQ
    Mongodb
    Java

## Dependencies
    https://mvnrepository.com/artifact/org.json/json
    https://mvnrepository.com/artifact/org.mongodb/mongo-java-driver
    https://mvnrepository.com/artifact/com.rabbitmq/amqp-client
    https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    https://mvnrepository.com/artifact/org.slf4j/slf4j-simple

## prerequisitions
    Before using this project, the user should first activate mongodb (use this command: sudo systemctl status mongod) and rabbitMQ server.

    
## RuleEngine
    RuleEngine class enables the user to check events received from a message broker against a set of user defined rules, and raise an alert in case      an event complies with a rule. Every event is logged in a Mongodb database. 


### How to Use RuleEngine class

1. Ctor :
     
   signature: ```public RuleEngine(String amqpUrlAlert, String queueNameAlert, String amqpUrl, String queueName, String dbUri, String dbName) throws IOException, TimeoutException```
   
    takes as parameters:
    @ data for establishing connection to rabbitmq queue for alert sending, through which the Alert microService will alert events matching    
    the defined rules.  
    @ data for establishing connection to rabbitmq queue for receiving events.
    @ data for for updating MongoDb database.
    
    in case of a failure during establishing the necessary connections - Ctor will 
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
    when user wants to shutdown the RuleEngine the shutdown will close all open sources.

    Example:
        ruleEngine.shutdown();
    
    
4. Add new rule
   Currently rules can be added through the 'rule.csv' file. The main concern is to enable non-software engeneers to add rules, without the need to write a piece of code in order to do so. Currently users can write rules before running the system. The primary intent is to create an observer that will observe this file, and updates occurs the observer will reload it, so new rule will be added automatically.
   
   In order to add new rule, the user should open 'rules.csv' and add new rule to it by this order:
   * rule-id(int unique id)
   * vehicle-type(String for example drone)
   * check-field(String field of event which contains the target value to check/ for example "cpu". In case of a logic rule this field should contain the key word "logic").
   * operator(String "greater"/"less"/"greater or equals"/"less or equals"/"equals") in case of logical rule the field should contain "and"/"or"
   * threshold(the value which is the threashold for the rule)
   * priority(String value between 1 - 3, specifies the ergency of the event)
   * message(String the message that would be sent to the alert micro service)
   * is-time-constrain (currently unavailable, value should be "false". This field is for future infrastructure to write rules with time constrain)
   * time-duration-seconds(currently unavailable)


    Example:
       1, drone, cpu, greater, 80, 1, cpu over 80, false, 0
       2, drone, battery, less, 30, 1, battery less then 30 percent, false, 0
       3, drone, long, greater, 32.08545236664342, 1, longtitude over 32.08545236664342, false, 0
       4, drone, lat, greater, 34.766925459287044, 1, latitude over 34.766925459287044, false, 0
       5, drone, logic, and ,3 - 4, 1, combination for rules 3 + 4 location, false, 0

### Badges

Add badges from somewhere like: [shields.io](https://shields.io/)

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)
[![AGPL License](https://img.shields.io/badge/license-AGPL-blue.svg)](http://www.gnu.org/licenses/agpl-3.0)

### Color Reference

| Color             | Hex                                                                |
| ----------------- | ------------------------------------------------------------------ |
| Example Color | ![#0a192f](https://via.placeholder.com/10/0a192f?text=+) #0a192f |
| Example Color | ![#f8f8f8](https://via.placeholder.com/10/f8f8f8?text=+) #f8f8f8 |
| Example Color | ![#00b48a](https://via.placeholder.com/10/00b48a?text=+) #00b48a |
| Example Color | ![#00d1a0](https://via.placeholder.com/10/00b48a?text=+) #00d1a0 |


### Features

- Light/dark mode toggle
- Live previews
- Fullscreen mode
- Cross platform

