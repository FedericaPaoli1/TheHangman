# TheHangman
[![Build Status](https://travis-ci.com/FedericaPaoli1/TheHangman.svg?branch=master)](https://travis-ci.com/FedericaPaoli1/TheHangman)
[![Coverage Status](https://coveralls.io/repos/github/FedericaPaoli1/TheHangman/badge.svg?branch=master)](https://coveralls.io/github/FedericaPaoli1/TheHangman?branch=master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=paoli.taverni%3Athehangman-aggregator&metric=alert_status)](https://sonarcloud.io/dashboard?id=paoli.taverni%3Athehangman-aggregator)

TheHangman is a project developed for the Advanced Techniques and Tools for Software Development (AATSW) exam. 
It implements the classic paper and pencil guessing game, called The Hangman.

## Getting Started
### Prerequisites

Docker service is required

### Installing
Once you've cloned the repository into your local machine, run the following command from the root directory of the project:

    docker build -t <image-name> .

This command creates a Docker MySql image, named `<image name>`, containing a default database, called _HangmanDB_.
Now, to create the final jar, run:

    ./mvnw -f thehangman-aggregator/pom.xml package -Papp
    
or you can run the following, without tests:

    ./mvnw -f thehangman-aggregator/pom.xml package -Papp -Dmaven.test.skip=true
    
 Before running the jar, make sure to start the MySql Docker image, previously created: 
 
    docker run --rm -it --name <MySql-container-name> -p <your-port>:3306 -e MYSQL_ROOT_PASSWORD=<your-password> -d <image-name>
    
You can run the jar with the following command:

    java -jar thehangman-app/target/thehangman-app-0.0.1-SNAPSHOT-jar-with-dependencies.jar \
      --mysql-port=<your-port> \
      --mysql-password \
      mode=<[graphical|terminal]>
      
## Running the tests

### Unit Tests

    ./mvnw -f thehangman-aggregator/pom.xml clean test 
    
### Integration Tests

    ./mvnw -f thehangman-aggregator/pom.xml clean verify
   
### E2E Tests

    ./mvnw -f thehangman-aggregator/pom.xml clean verify -Papp
    
### Mutation Tests
    
    ./mvnw -f thehangman-aggregator/pom.xml clean verify -Pjacoco,mutation-testing
    
### Code Coverage
Reports available on folder thehangman-report/target/site/jacoco-aggregate
    
    ./mvnw -f thehangman-aggregator/pom.xml clean verify -Pjacoco

## Authors

- Federica Paolì
- Stefano Taverni
