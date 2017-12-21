# HTTP Server in Java

## Requirements

* Java 1.8
* [Gradle](https://gradle.org/) 4.3

## About the Server

The server was programmed in Java and can be run using applications built to server specifications.  See **Using your own Application** for more information.

## Running the Sample Cob Spec Application

Get the code from GitHub:

`git clone git@github.com:saralein/java-server.git`

Change your directory to cobspec:

`cd java-server/cobspec`

### From Gradle:

* For default parameters, `gradle -q run`.
    * Server will run on port 5000.
    * Server will serve from the `public` folder in cobspec.
* To specify parameters, you may provide one or both of the following options:
    * To specify a port, you many use the `-p,port` option, where `port` is replaced with a number between 1 and 65535.
        * Example: `gradle -q run -PrunArgs=-p,6066`.
    * To specify a directory, you many use the `-d,directory` option, where `directory` is replaced by the directory you wish to serve.
        *Example: `gradle -q run -PrunArgs=-d,my-dir`.
    * Example with both options: `gradle -q run -PrunArgs=-p,6066,-d,my-dir`.

### From a JAR:

* First build the JAR with `gradle build`.
* For default parameters, run the JAR with `java -jar build/libs/cobspec-1.0-SNAPSHOT.jar`.
    * Server will run on port 5000.
    * Server will serve from the `public` folder in cobspec.
* To specify parameters, you may provide one or both of the following options:
    * To specify a port, you may use the `-p port` option, where `port` is replaced with a number between 1 and 65535.
        * Example: `java -jar build/libs/cobspec-1.0-SNAPSHOT.jar -p 5000`.
    * To specify a directory, you may use the `-d directory` option, where `directory` is replaced by the directory you wish to serve relative to your home directory.
        * Example: `java -jar build/libs/cobspec-1.0-SNAPSHOT.jar -d my-dir`.
    * Example with both options: `java -jar build/libs/cobspec-1.0-SNAPSHOT.jar -p 5000 -d my-dir`.

## Stopping the Server

For either run method, type `ctrl + c`.

## Using your own Application

[See the README](UsingTheServer.md)