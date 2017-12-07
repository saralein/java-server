# Using Your Own Application

The server can be setup to run with your application.

## Updating your application build.gradle

The [server JAR](https://bintray.com/saralein/server) is hosted on Bintray and linked to jcenter. It can be accessed by adding the following to your application build.gradle.

In your dependencies:

```
dependencies {
    compile 'com.saralein:server:1.0.01'
}
```

In your repositories:

```
repositories {
    jcenter()
}
```

## Using the server

Usage of the server requires some setup within your application.

Your application should call `HttpServer start` in order to run the server. Below you will find documentation for `HttpServer` and other classes required for setup.

**Class HttpServer**

| Type                 | Method                                                     |
| -------------------- | ---------------------------------------------------------- |
| `public static void` | `start(int port, Path root, Routes routes, Logger logger)` |

The server requires a port between 1 and 65535, and a valid root directory. 


**Routes**

| Type          | Method                                                        |
| ------------- | ------------------------------------------------------------- |
| `constructor` | `Routes(`<br>`HashMap<String, HashMap<String, Controller>> routes,`<br>`Controller directoryController,`<br>`Controller fileController,`<br>`ErrorController errorController)`                  |

The hashmap follows the structure `HashMap<String, HashMap<String, Controller>>`, where the key is your route URI and the associate hashmap is a method ("GET", "POST", "PUT", etc.) and the controller which handles the specific method for that URI.
   
For example, if you wanted to have "GET" and "POST" requests for a "/board" route, your routes hashmap would be:
   
   ```
   {"/board":
     {
       "GET": Controller,
       "POST": Controller
     }
   }
   ```
   
 In addition to a hashmap, `Routes` requires three controllers: 
 
 1. Directory controller: handles directory requests
 2. File controller: handles file requests
 3. Error controller: handles client side errors in the application, such as 404s and 405s  
   
**Interface Controller**

| Type              | Method                            |
| ----------------- | --------------------------------- |
| `public Response` | `createResponse(Request request)` |

See [Example Controller](#example-controller) for more information.

**Response**

| Type            | Method                                 |
| --------------- | -------------------------------------- |
| `constructor`   | `Response(Header header, byte[] body)` |
| `public Header` | `getHeader()`                          |
| `public byte[]` | `getBody()`                            |

**ResponseBuilder**

| Type                     | Method                                   |
| ------------------------ | ---------------------------------------- |
| `public ResponseBuilder` | `addBody(String body)`                   |
|                          | `addBody(byte[] body)` <br><br>Add body to response.  Overloaded string variation will convert body to a byte array. |
| `public ResponseBuilder` | `addStatus(int code)` <br><br> Adds HTTP status code to header response line. |
| `public ResponseBuilder` | `addHeader(String title, String content` <br><br> Adds individual headers to Header instance. For example, `addHeader(Content-Type, text/html)` adds Content-Type: text/html to the header. |
| `public Response`        | `build()` <br><br>Creates a `Response` instance with information added via other `ResponseBuilder` methods. |

**Header**

| Type | Method |
| ---- | ------ |
| `public void` | `addStatus(int code)` <br><br>Adds HTTP status code to header response line. |
| `public void` | `addHeader(String title, String content)` <br><br>Adds individual headers to Header instance. For example, `addHeader(Content-Type, text/html)` adds Content-Type: text/html to the header. |
| `public String` | `formatToString()` <br><br>Returns the full header formatted for HTTP. |

**Interface ErrorController**

The `ErrorController` interface extends the `Controller` interface.  Implementations should implement `Controller` and `ErrorController` methods.

| Type                     | Method                     |
| ------------------------ | -------------------------- |
| `public ErrorController` | `updateStatus(int status)` <br><br>The server uses this method to update status code based on the type of client error. |

**Interface Logger**

| Type          | Method               |
| ------------- | -------------------- |
| `public void` | `log(String status)` |

### Example Controller

An example `Controller` from the cobspec application is provided below:

```
public class RedirectController implements Controller {
    public Response createResponse(Request request) {
        return new ResponseBuilder()
                    .addStatus(302)
                    .addHeader("Location", "/")
                    .build();
    }
}
```

Use of the `ResponseBuilder` is not required but is provided for convenience.

Your `Controller` may contain whatever additional methods are needed to create contents of your response.
