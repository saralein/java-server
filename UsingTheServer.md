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

## Using the Server

Usage of the server requires some setup within your application.

Begin by creating an instance of the HttpServer (see [API](#api) below for `Logger` information): `HttpServer(Logger logger)`. From here, you can being configuring your server.

### `.addStatic(Path root)`

`addStatic` sets up built-in middleware for serving static files.  The `root` parameter specifics the root directory from which static resources will be served.  If a resource matching the requested resource is not found in the root directory, the request passes to the router to check for matching routes before returning a 404.

Static middleware will not be included if `addStatic` is not set.

### `.router(Routes routes)` 

`router` creates an instance of the server router which uses the provided routes. The router is the last layer for matching request resources.  If a match is found for the requested resource, the router will use the controller specified in `routes` (see [Setting Up Routes](#setting-up-routes)). If the router does not find a route matching the requested resource, it will return a 404.

A route-less router will be used if `router` is not set.

### `.start(int port)`

Once the above methods are used to configure the server, use `start` with a valid port number to start the server.

## Example Setup

Below is a simple example of how server setup might look:

```java
new HttpServer(logger)
     .addStatic(root)
     .router(new Routes()
                  .get("/redirect", new RedirectController())
                  .get("/form", new FormController())
                  .post("/form", new FormController())
                  .put("/form", new FormController())
                  .delete("/form", new FormController())
                  .get("/logs", new LogController()))
     .start(port);

```

Notes: Controllers are application specific.

## Setting Up Routes

To set up routes for the server, create an instance of Routes: `new Routes()`.

Specific routes can be added to the routes instance using the following methods:

`.get(String uri, Controller controller)`    
`.post(String uri, Controller controller)`    
`.put(String uri, Controller controller)`    
`.head(String uri, Controller controller)`    
`.options(String uri, Controller controller)`    
`.delete(String uri, Controller controller)`    

Methods correspond to their respective HTTP method: GET, POST, PUT, HEAD, OPTIONS, DELETE.

### Example Routes

As an example, to create a routes instance which handles a get request to "/about" and a post request to "/form", we would configure our routes as follows:

```java
new Routes()
     .get("/about", aboutController)
     .post("/form", formController)
```

##Setting Up Controllers

Controllers must implement the `Controller` interface.

### Example Controller

An example `Controller` from the cobspec application is provided below:

```java
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

## API

The following section provides additional details on the public API for the HTTP server.

**Class HttpServer**

| Type                 | Method                                                     |
| -------------------- | ---------------------------------------------------------- |
| `constructor`        | `HttpServer(Logger logger)`                                |
| `public HttpServer`  | `addStatic(Path root)`                                     |
| `public HttpServer`  | `router(Routes routes)`                                    |
| `public HttpServer`  | `use(Middleware middleware)`                               |
| `public static void` | `start(int port)`                                          |

**Class Routes**

| Type            | Method                                                        |
| --------------- | ------------------------------------------------------------- |
| `public Routes` | `get(String uri, Controller controller)`                      |
| `public Routes` | `post(String uri, Controller controller)`                     |
| `public Routes` | `put(String uri, Controller controller)`                      |
| `public Routes` | `head(String uri, Controller controller)`                     |
| `public Routes` | `options(String uri, Controller controller)`                  |
| `public Routes` | `delete(String uri, Controller controller)`                   |

**Interface Controller**

| Type              | Method                            |
| ----------------- | --------------------------------- |
| `public Response` | `createResponse(Request request)` |

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

**Interface Logger**

| Type          | Method                     |
| ------------- | -------------------------- |
| `public void` | `info(String status)`      |
| `public void` | `exception(Exception e)`   |
| `public void` | `request(Request request)` |