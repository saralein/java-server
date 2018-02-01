package com.saralein.server.router;

import com.saralein.server.controller.Controller;
import com.saralein.server.middleware.Callable;
import com.saralein.server.middleware.Middleware;
import com.saralein.server.protocol.StatusCodes;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class Router implements Middleware {
    private final Routes routes;
    private Callable next;

    public Router(Routes routes) {
        this.routes = routes;
        this.next = null;
    }

    @Override
    public Middleware apply(Callable callable) {
        next = callable;
        return this;
    }

    @Override
    public Response call(Request request) {
        String uri = request.getUri();
        String method = request.getMethod();

        if (routes.matchesRouteAndMethod(uri, method)) {
            Controller controller = routes.retrieveController(uri, method);
            return controller.respond(request);
        } else if (routes.matchesRouteButNotMethod(uri, method)) {
            return accessNotAllowed();
        } else {
            return next.call(request);
        }
    }

    private Response accessNotAllowed() {
        return new Response.Builder()
                .status(405)
                .addHeader("Content-Type", "text/html")
                .body(StatusCodes.retrieve(405))
                .build();
    }
}
