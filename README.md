# HTTP Server in Java

## Requirements

* Java
* [Gradle](https://gradle.org/)

## Running

Get the code from GitHub:

`git clone git@github.com:saralein/java-server.git`    

`cd java-server`

### From Gradle:

* `gradle -q run`
* In your browser, go to `localhost:1337`.

### From a JAR:

* First build the JAR with `gradle jar`.
* This creates a JAR in `build/libs`.
* In the `build/libs` directory, run the JAR with `java -jar {JAR_NAME).jar 1337`.
* In your browser, go to `localhost:1337`.

## Stopping the Server

For either run method, type `ctrl + c`.