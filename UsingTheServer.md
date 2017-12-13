# Using Your Own Application

The server is currently set up to run with a default cobspec application.  This guide will help you set up the server for use with your own application.

## Adding a module

Much like the cobspec application, your application should be added as a module to the project structure.

httpServer    
|_ cobspec      
|_ server   
|_ YOUR_APPLICATION

Set up your module with Gradle using GroupID `com.saralein` and ArtifactID `YOUR_APPLICATION`, where `YOUR_APPLICATION` is whatever name you choose.

Your module should contain a build.gradle.  settings.gradle in the project root should be modified to include your application:

`rootProject.name = 'httpServer'`    
 `include 'server', 'cobspec', 'YOUR_APPLICATION'`    

## Updating your application build.gradle

The build.gradle in your application module should be updated to include the following:

```
group 'com.saralein'
version '1.0'
   
apply plugin: 'java'
apply plugin: 'application'
   
mainClassName = 'com.saralein.YOUR_APPLICATION.Main'
sourceCompatibility = 1.8
   
dependencies {
    compile project(':server')
    testCompile group: 'junit', name: 'junit', version: '4.12'
}
   
repositories {
    mavenCentral()
}
   
jar {
    manifest {
        attributes 'Main-Class': 'com.saralein.YOUR_APPLICATION.Main'
    }

    from {
        configurations.compile.collect { it.isDirectory() ? it : zipTree(it) }
    }
}
    
run {
    if (project.hasProperty("runArgs")) {
        args(runArgs.split(','))
    }
}
```

## Setting up your application Main

Usage of the server requires some setup within your application.

1. Within `main` in your application `Main` class, you should specify a base location from which you plan to server files. In the cobspec application, this location is `user.dir`.  Note that the specific directory from which you plan to serve is appended via the `args` passed into `main`.
2. The server `HttpServer start` method requires the following arguments:
    
    `main(String[] args, String home, Function<Path, Routes> setupRoutes)`
    
    Pass your application `args` as the first argument and your base serving location as the second `home` argument.
    
    To satisfy the third argument, your application must pass a function to the server `main` which accepts a `Path` as its argument as returns an instance of the `server` `Routes` class.
    
### Routes

Minimal setup of a `Routes` instance requires instantiation with four arguments:

1. Routes hashmap - follows the structure `HashMap<String, HashMap<String, Controller>>`, where the key is your route URI and the associate hashmap is a method ("GET", "POST", "PUT", etc.) and the controller which handles the specific method for that URI.
   
   For example, if you wanted to have "GET" and "POST" requests for a "/board" route, your routes hashmap would be:
   
   ```
   {"/board":
     {
       "GET": Controller,
       "POST": Controller
     }
   }
   ```
2. Directory controller - handles directory requests, implements the server `Controller` interface
3. File controller - handles file requests, implements the server `Controller` interface
4. Not found controller - handles 404s on absent resources, implements the server `Controller` interface

For ease of use, you may choose to follow the `ResponseBuilder` implementation in the cobspec application for creating your routes.

### Controllers

Your controllers should implement the server `Controller` interface:

```java
public interface Controller {
    Response createResponse(Request request);
}

```

For example controllers/responses, see the cobspec controllers.

### Example Implementation

With your function setup, simply pass `args`, `home`, and your function to the server `main` function.  Below is an example of how your main might look:

```java
public class Main {
    public static void main(String[] args) {
        String home = System.getProperty("user.dir");
        HttpServer.start(args, home, Main::setupRoutes);
    }
    
    private static Routes setupRoutes(Path root) {
        FileHelper fileHelper = new FileHelper(root);
        FormStore formStore = new FormStore();
        FormBody formBody = new FormBody();
        FormModification formModification = new FormModification();
    
        return new RoutesBuilder(new DirectoryController(fileHelper), new FileController(fileHelper), new NotFoundController())
                    .addRoute("/redirect", "GET", new RedirectController())
                    .addRoute("/form", "GET", new FormGetController(formStore, formBody))
                    .addRoute("/form", "POST", new FormPostController(formStore, formBody, formModification))
                    .addRoute("/form", "PUT", new FormPutController(formStore, formBody, formModification))
                    .addRoute("/form", "DELETE", new FormDeleteController(formStore))
                    .build();
    }
}
```

## Building your application JAR

With your application build.gradle updated and `main` set up, run `gradle build` to build your application JAR to `YOUR_APPLICATION/build/libs`.

## Running your application JAR

* For default parameters, run the JAR with `java -jar YOUR_APPLICATION/build/libs/YOUR_APPLICATION-1.0.jar` in the `YOUR_APPLICATION/build/libs` directory.
    * Server will run on port 5000.
    * Server will serve from the `Public` folder of your home directory.
* To specify parameters, you may provide one or both of the following options:
    * To specify a port, you may use the `-p port` option, where `port` is replaced with a number between 1 and 65535.
        * Example: `java -jar YOUR_APPLICATION/build/libs/YOUR_APPLICATION-1.0.jar -p 5000`.
    * To specify a directory, you may use the `-d directory` option, where `directory` is replaced by the directory you wish to serve relative to your home directory.
        * Example: `java -jar YOUR_APPLICATION/build/libs/YOUR_APPLICATION-1.0.jar -d my-dir`.
    * Example with both options: `java -jar YOUR_APPLICATION/build/libs/YOUR_APPLICATION-1.0.jar -p 5000 -d my-dir`.
    
## Stopping the Server

For either run method, type `ctrl + c`.