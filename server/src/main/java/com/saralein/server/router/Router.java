package com.saralein.server.router;

import com.saralein.server.middleware.Callable;
import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class Router implements Callable {
    private final Routes routes;

    public Router(Routes routes) {
        this.routes = routes;
    }

    public Response call(Request request) {
        String uri = request.getUri();
        String method = request.getMethod();

        if (routes.matchesRouteAndMethod(uri, method)) {
            Controller controller = routes.retrieveController(uri, method);
            return controller.respond(request);
        } else if (routes.matchesRouteButNotMethod(uri, method)) {
            return routeError(405);
        } else {
            return routeError(404);
        }
    }

    private Response routeError(int code) {
        String body = code == 404 ? "404: Page not found." : "";

        return new Response.Builder()
                .status(code)
                .addHeader("Content-Type", "text/html")
                .body(body)
                .build();
    }
}
