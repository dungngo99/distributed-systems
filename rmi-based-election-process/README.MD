## Project
This project is a brief introduction to Remote Method Invocation (RMI), a Java library to create and facilitate the communication among distributed components.
The project is about simulating a voting system, where many clients can interact with a server to vote, query their profiles, and check the current winner of a race.

Java RMI is built on top of TCP, and by programming with Interface, it abstracts away the extra work such as dealing with networking protocols or connection between servers/clients in Socket Programming.
Instead, we can focus more on the business logic of our application.

### Brief introduction to Java RMI
RMI follows the basic request-reply communication. In fact, a client sends its requests to server by invoking remote methods of a remote object implemented by the server.
The server will receive the requests, process it, and then send back the results.

Different from the local method invocation, remote method invocation involves one process to call methods of other processes, regardless of the fact that these processes are in the same machine or not. A remote methods are called from a remote object, which is passed by reference from the registry.

Registry is the key in Java RMI, where it acts as an intermediate between server and clients. Registry is similar to a map data structure where it maps a textual name to a remote object reference. Therefore, after a server has created remote objects and add them to the registry, clients can look up object reference by specific names.

### Project structure
There are 3 main files, Election.java, Server.java, and Client.java.
Election is an interface that extends from Remote Class.
Server is a class that implements Election interface.
Client is a class to set up the client UI

### How to run the program
Please follow these steps to run the program after cloning the repo back to your local machine.
1. Go to ./src and compile the files as javac -D class *.java
2. Go to ./class and prepare at least 3 terminal windows (one for rmi registry, one for server, and at least one for clients)
3. Type rmiregistry in 1st window
4. Type java Server on the 2nd window
5. Type java Client on the 3rd window (You can open more windows for more clients)
6. Follow the instructions of the program and observe the output of both Client and Server

### Further improvements and comments
Since this is a small class project, there are many things that I need to improve on so please let me know if you find anything interesting in my project. Thank you ^^!