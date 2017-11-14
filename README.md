# HTTP Server in Java

## Requirements

* Java
* [Gradle](https://gradle.org/)

## Running

Get the code from GitHub:

`git clone git@github.com:saralein/java-server.git`    

`cd java-server`

### From Gradle:

* For default parameters, `gradle -q run`.
    * Server will run on port 1337.
    * Server will serve from the `Public` folder of your home directory.
* To specify parameters, `gradle -q run -PrunArgs=port,directory`.
    * Replace `port` with a number between 1 and 65535.
    * Replace `directory` with the directory you wish to server relative to your home directory.

### From a JAR:

* First build the JAR with `gradle jar`.
* This creates a JAR in `build/libs`.
* For default parameters, run the JAR with `java -jar (JAR_NAME}.jar` in the `build/libs` directory.
    * Server will run on port 1337.
    * Server will serve from the `Public` folder of your home directory.
* To specify parameters, run the JAR with `java -jar {JAR_NAME}.jar port directory` in the `build/libs` directory.
    * Replace `port` with a number between 1 and 65535.
    * Replace `directory` with the directory you wish to server relative to your home directory.

## Stopping the Server

For either run method, type `ctrl + c`.