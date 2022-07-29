This Project is just a demo/showcase for understanding of Spring Boot / Project Reactor  Concepts.


   +src/main/java

    +com.rahmanash.gettingstarted.apiservice
      -ApiService.java
      -ApiServiceApplication.java
      -ApiServiceController.java
      
      +config
        -ExecutorConfig.java
       
      +H2
        -H2.java
        
      +room
        -Room.java
        -RoomController.java
        -RoomRepository.java
        
        +roomstats
            -RoomStats.java
            -RoomStatsProducerService.java
            -RoomStatsConsumerService.java
        
      +threadexecutors
        -ThreadExecutorService.java
        
        
This Spring Boot Application is basically an extremely basic version of REST API Service.Here, services can contact this application to create,fetch and delete Rooms.For Simplicity,services has been hardcoded in application properties file and no authentication mechanism is provided

ApiService gets valid services list from app properties and get's injected in its properties on start. It has small method, where it checks for the presence of serviceId in it's valid services list.

H2 Mem Database is used for persistance and is transactional through JPA Concepts. And, H2 Webservice is started mannually  with H2 java file through the H2 API's since WebFlux Starter and H2 console was incompatible and can't be started

ExecutorConfig - This project has a Basic Config through which we create a FixedThreadPool and exposed as a Spring Bean. This bean will be used for all Executor Servcies

ThreadExecutorService is a Component  which  helps to run the task/process in a thread(fetched from Executor Service Config) , separate from Current Thread and returns a Completable Future. This means the process won't block the Request thread and will complete the future after process successfully ran.

Room package has the components for the REST Service implementation. It has a Room Model based on JPA Entity and a DAO Repository which access the database configured for this project. 

The Rest Controller is the entry point for handling all REST API Requests. It supports 4 Requests as of now.
  - Fetch all Rooms of a Service
  - Fetch all Rooms based on Id's (array of Id's given in request params)
  - Create Room for a Service
  - Delete All Rooms for a Service

All Methods uses ThreadExecutorService to run its query with database. Since transactions with database is blocking, all DB transactions can be pushed to a separate thread pool where it can ran and complete. Supplier function is provided to executor service to exexcute these transactions. These Methods makes use of Reactor Mono and Flux Concepts to handle Futures and Responses.

And, there is one derived Query Method and Custom Query Methods in Room Repository for DB Transactions, apart from default query methods provided out of the box by JPA.

For showcasing of implementation of Reactive Streams, a pub/sub service for updating stats of a room is implemented. This uses Sink(a reactive stream implementation of publisher and subscriber ) to emit and transmit Objects to multiple Subscribers at the same time.

In this project, stats of a room creation/deletion is pushed to a dummy queuing service/ dummy database service using the Sink.

