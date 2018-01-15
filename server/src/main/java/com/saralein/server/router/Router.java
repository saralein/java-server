package com.saralein.server.router;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class Router implements Controller {
    private final Routes routes;

    public Router(Routes routes) {
        this.routes = routes;
    }

    public Response createResponse(Request request) {
        String uri = request.getUri();
        String method = request.getMethod();

        if (routes.matchesRouteAndMethod(uri, method)) {
            Controller controller = routes.retrieveController(uri, method);
            return controller.createResponse(request);
        } else if (routes.matchesRouteButNotMethod(uri, method)) {
            return routeError(405);
        } else {
            return routeError(404);
        }
    }

    private Response routeError(int code) {
        return new Response.Builder()
                .addStatus(code)
                .addHeader("Content-Type", "text/html")
                .addBodyByStatus(code)
                .build();
    }
}
