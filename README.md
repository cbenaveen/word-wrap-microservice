## Word Wrap Microservice

### Introduction
A microservice implementation, which wraps the given contentRequest into chunks of words as per the given size.

#### Example:
##### Input Text:
Develop a word wrap micro service which provides functionality to take an input string and wraps it so that none of the lines are longer than the max length. The lines should not break any word in the middle. 
##### Max Length:
23

##### Output
Design a word wrap \n
micro service which \n
provides functionality \n
to take an input string \n
and wraps it so that \n
none of the lines are \n
longer than the max \n
length. The lines \n
should not break any \n
word in the middle. 

### Functional Requirements
1) Expose Restful API and Websocket to allow the client access the microservice
2) The Service should be able to handle high concurrent request 

### Non Functional Requirements
1) The service should expose adequate metrics for the monitoring capability
2) Up to 80% code coverage during the Unit Testing
3) The application should be wrapped as container to be able to run anywhere

### Design Consideration
1) The service should follow the Microservice architecture patterns
2) The service should be reactive in nature
3) Storage abstraction needs to be provided in case if the given input needs to preserved if the client want to access the chunks incrementally
4) Modular and layered approach
5) DDoS Protection




