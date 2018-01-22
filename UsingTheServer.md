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

Usage of the server requires some setup within your application.  Begin by creating an instance of the `Application` using the `Application.Builder`, which includes the following methods:

### `.router(Routes routes)` 

`router` creates an instance of the server router which uses the provided routes. The router is the last layer for matching request resources.  If a match is found for the requested resource, the router will use the controller specified in `routes` (see [Setting Up Routes](#setting-up-routes)). If the router does not find a route matching the requested resource, it will return a 404.

A route-less router will be used if `router` is not set.

### `.use(Middleware middleware)`

`use` adds middleware to your application, which is applied over the default static middleware and router.

## `.build()`

`build` builds an instance of `Application` using the specified router/middleware configuration.

## Example Setup

Below is a simple example of how application setup might look:

```java
new Application.Builder(logger, root)
     .router(new Routes()
                  .get("/redirect", new RedirectController())
                  .get("/form", new FormController())
                  .post("/form", new FormController())
                  .put("/form", new FormController())
                  .delete("/form", new FormController())
                  .get("/logs", new LogController())
                  .use("/logs", routeConfig))
     .use(middelware)
     .build();
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

Route configuration for use with middleware can be added using `use`:

`.use(String uri, RouteConfig routeConfig)`

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
        return new Response.Builder()
                    .status(302)
                    .addHeader("Location", "/")
                    .build();
    }
}
```

Use of the `Response.Builder` is not required but is provided for convenience.

Your `Controller` may contain whatever additional methods are needed to create contents of your response.

## Setting Up Middleware

You may choose to create your own middleware for your application. In order to do so, your middleware should extend the `Middleware` abstract class.

`Middleware` provides the implementation for the `apply` method, which applies your middleware over the server default middleware.

In turn, `Middleware` implements the `Caller` interface.  You middleware will need to provide implementation for the `call` method.

## Example Middleware

```java
public class AuthMiddleware extends Middleware {
    ...

    @Override
    public Response call(Request request) {
        if (isAuthorized(request)) {
            return middleware.call(request);
        } else {
            return unauthorized();
        }
    }

    ...
}
```

`middleware` (the cumulative application of middlewares over the server default middleware) is set in the `apply` method of `Middleware`.

The above authorization middleware (which comes with the server for application use) checks if an incoming request is authorized to access a route. (It checks username/password set in the `RouteConfig` for that route.)  If not authorized, it returns a 401 response.  If so, it calls the cumulative middleware and sends the request in to eventually be handled by the router.

## After You Create Your Application

After your application instance is created, use the `ServerInitialzer` `setup` method to create an instance of the server which uses your application.

```
Server server = new ServerInitializer(logger, application).setup(port);
```

From here, you can call `server.run()` to run your application on the server.

## API

The following section provides additional details on the public API for the HTTP server.

**Class Application.Builder**

| Type                 | Method                                                     |
| -------------------- | ---------------------------------------------------------- |
| `constructor`        | `Application.Builder(Logger logger, Path roo)`             |
| `public Builder`     | `router(Routes routes)`                                    |
| `public Builder`     | `use(Middleware middleware)`                               |
| `public Application` | `build()`                                                  |

**Class Application**

| Type                 | Method                                                     |
| -------------------- | ---------------------------------------------------------- |
| `public Response`    | `call(Request request)`                                    |

**Class Routes**

| Type            | Method                                                        |
| --------------- | ------------------------------------------------------------- |
| `public Routes`      | `get(String uri, Controller controller)`                 |
| `public Routes`      | `post(String uri, Controller controller)`                |
| `public Routes`      | `put(String uri, Controller controller)`                 |
| `public Routes`      | `head(String uri, Controller controller)`                |
| `public Routes`      | `options(String uri, Controller controller)`             |
| `public Routes`      | `delete(String uri, Controller controller)`              |
| `public Routes`      | `use(String uri, RouteConfig routeConfig)`               |
| `public RouteConfig` | `getConfig(String uri)`                                  |

**Class RouteConfig**

| Type                 | Method                                                   |
| -------------------- | -------------------------------------------------------- |
| `public RouteConfig` | `add(String key, String value)`                          |
| `public String`      | `getValue(String key)`                                   |

 
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

**Response.Builder**

| Type                     | Method                                   |
| ------------------------ | ---------------------------------------- |
| `public Builder`         | `body(String body)`                   |
|                          | `body(byte[] body)` <br><br>Add body to response.  Overloaded string variation will convert body to a byte array. |
| `public Builder`         | `status(int code)` <br><br> Adds HTTP status code to header response line. |
| `public Builder`         | `addHeader(String title, String content)` <br><br> Adds individual headers to Header instance. For example, `addHeader(Content-Type, text/html)` adds Content-Type: text/html to the header.                  |
| `public Response`        | `build()` <br><br>Creates a `Response` instance with information added via other `Builder` methods. |

**Header**

| Type | Method |
| ---- | ------ |
| `public void` | `status(int code)` <br><br>Adds HTTP status code to header response line. |
| `public void` | `addHeader(String title, String content)` <br><br>Adds individual headers to Header instance. For example, `addHeader(Content-Type, text/html)` adds Content-Type: text/html to the header. |
| `public String` | `formatToString()` <br><br>Returns the full header formatted for HTTP. |

**Class Middleware**

| Type                 | Method                                 |
| -------------------- | -------------------------------------- |
| `final Middleware`   | `apply(Caller caller)`                 |

**Interface Caller**

| Type                 | Method                                 |
| -------------------- | -------------------------------------- |
| `public Response`    | `call(Request request)`                |

**Class ServerInitializer**

| Type                 | Method                                                                     |
| -------------------- | -------------------------------------------------------------------------- |
| `constructor`        | `ServerInitializer(Logger logger, Application application)`                |
| `public Server`      | `setup(int port)`                                                          |

**Class Server**

| Type                 | Method       |
| -------------------- | ------------ |
| `public void`        | `run()`      | 

**Interface Logger**

| Type          | Method                     |
| ------------- | -------------------------- |
| `public void` | `error(Exception e)`       |
| `public void` | `fatal(String message)`    |
| `public void` | `info(String message)`     |
| `public void` | `trace(Request request)`   |