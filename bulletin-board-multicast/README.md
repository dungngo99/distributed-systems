### Objective
This lab implements a simple bulletin board where multiple users can post their messages, and messages are sent to or multicasted to other users.
Technologies: Java and UDP multicast via socket programming

### Requirements
1. Multiple users can post messages using CLI (checked - refer to "How to run" section) 
   ####
2. At least 4 processes can join a group, where they can post and receive messages in an ordered multicast (checked - refer to "How to run" section)
   ####
3. Multicast supports total ordering + causal ordering (just FIFO ordering)
   ####
4. Each process has multicast interface (multicast method + receive method) (checked - refer to the code)
   ####
5. App layer: 1 process sends messages -> display messages on others ends (checked - refer to the code)
   ####
6. Have an interface for the sequencer (not checked)
   ####
7. All processes in the same group, group members are fixed, no group id is needed. (checked - refer to command arguments)
   ####
8. Use UDP sockets to implement message passing, and processes are given different port number (checked - refer to the code)
   ####
9. Channels are reliable -> simulate channel delays. 
   Random transmission delay should be added to each message (checked - refer to the code)
   
### Workload
Each user when join a group is considered as 1 process that has 2 threads. 
One thread (main thread) will send messages, and another thread of RunThread object will receive message
The workload is distributed evenly among users.

### How to run
1. Download and extract the .zip file
   ####
2. Open the Terminal(macOS) / Command Line (Linux/Windows) and redirect the root directory of the extracted file. (Ex: .../lab2)
   ####
3. Change directory to ./src folder
   ####
4. Recompile the source code as following: </br> 
   javac BotUDP.java MyPacket.java ReadThread.java -Xlint:deprecation
   ####
5. Open 3 more terminal windows and redirect to ./src file
   ####
6. For each window, run the code as: </br>
   java BotUDP 239.0.0.0 1234 <num>
   ###
Note:
   1. 239.0.0.0 is Ipv4 address used for multicasting </br>
   2. To run the code, 3 command arguments must be provided 
   3. num will be from 0 to 3 </br>
   4. to exit a program, type "exit" </br>