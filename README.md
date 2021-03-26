# Assignment

Implement an application which represents a store for car charging session entities. It will hold all records in memory and provide
REST API.
Each entity of the store represents unique charging session that can be in progress or finished. Entity can have the following fields:

```
UUID id;
String stationId;
LocalDateTime startedAt;
LocalDateTime stoppedAt;
StatusEnum status;
```

![Alt text](src/main/resources/static/endpoint1.png?raw=true "Optional Title")
![Alt text](src/main/resources/static/endpoint2.png?raw=true "Optional Title")

## Requirements

1. Implementation should be done in Java 8, but feel free to use any libraries or frameworks you want.  
2. Application is thread-safe.  
3. Application is covered with tests (classes and endpoints).  
4. Application is using in-memory data structures (not to be confused with in-memory databases).  
5. Computational complexity meets our requirements (see the table). Limits are only applicable to the data structure which holds charging session objects (serialization, object mappings and other parts of application logic are out of consideration and may have arbitrary complexity).  
6. Documentation of the implemented functionality and instructions how to run are present (consider adding Javadoc and README file). We expect it to be run with a single command.  
7. It is important that your application meets all the requirements for successfully passing the assignment.  

## My Implementation

* It's built for **SpringBoot 2.4.2**

## Requirement
* Java 8
* Spring Boot
* Maven
* Swagger API Documentation


## How to Run application

* Build the project by running `mvn clean package` inside root directory.
* Once successfully built, run the service by using the following command:

**Before running app, ensure that port 8090 is free. One can use `server.port` system property in `application.yml` to override default 8090 port**

Go into directory `ChargingSession` root and run:

```
./mvnw spring-boot:run
```
The command downloads maven(if required, only first time), builds and runs workbook web-app. If everything goes well, last line in the console will be

```
2021-03-20 20:11:53.144  INFO 7429 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2021-03-20 20:11:53.297  INFO 7429 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8090 (http) with context path ''
```

You can also run the application by using below command from root directory:

```
java -jar  target/chargingsessionservice-0.0.1-SNAPSHOT.jar
```

To use API by running it locally you can use `Postman` and attached postman collection. You can find postman collection file inside root directory.

```
ChargingSession API.postman_collection.json
```

## REST APIs Endpoints
### Submit a new charging session for the station   
```
POST /chargingSessions  
Accept: application/json  
Content-Type: application/json  
```
![Alt text](src/main/resources/static/create.png?raw=true "Optional Title")

### Stop charging session
```
PUT /chargingSessions/{id}
```
![Alt text](src/main/resources/static/stopsession.png?raw=true "Optional Title")

### Retrieve All chargingSessions
```
GET /chargingSessions
```
![Alt text](src/main/resources/static/getallsessions.png?raw=true "Optional Title")

### Retrieve a summary of submitted charging sessions
```
GET /chargingSessions/summary
```
![Alt text](src/main/resources/static/summary.png?raw=true "Optional Title")

### To view Swagger 2 API docs
```
Run the server and browse to - http://localhost:8090/swagger-ui.html#/charging-session-controller
```
![Alt text](src/main/resources/static/swagger.png?raw=true "Optional Title")





