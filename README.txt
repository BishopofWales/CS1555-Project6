Brandon Donahue, Michael Hermenault, John Jankowski
Term Project
December 9th, 2018

MyAuction.java

Compilation: javac -classpath '.;.\ojdbc6.jar' MyAuction.java
Running: java -classpath '.;.\ojdbc6.jar' MyAuction

The user will be prompted from the command line to enter their login credentials. From there, there will be on screen instructions
detailing how to interact with the system. To pick an option from a list, simply type the corresponding letter/number and hit enter.
The system will tell the user if they entered an invalid option, and will also display any errors experienced when attempting to interact 
with the database. For each option selected from the menus, there will be on screen instructions on what to type into the system.

Driver.java 

Compilation: javac -classpath '.;.\ojdbc6.jar' Driver.java
Running: java -classpath '.;.\ojdbc6.jar' Driver

Upon running, the driver program will begin calling the various functions used in MyAuction. There is a pre-determined set of test cases that the
driver will execute. Printed to the screen will be a message saying what the driver is about to do, and the success/error messages that go with the 
operations. This file is meant to demonstrate that our system can handle invalid inputs, and will spit out error messages during the running of driver.
This is meant to show that our system will not crash, even when invalid data is entered.

Benchmark.java

Compilation: javac -classpath '.;.\ojdbc6.jar' Benchmark.java
Running: java -classpath '.;.\ojdbc6.jar' 

The Benchmark file is meant to demonstrate the large amount of data that our system can handle at a given time. This file will behave in a similar manner to
driver.java, but will call each test case multiple times to stress test the system. 

