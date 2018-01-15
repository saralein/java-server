package com.saralein.server.router;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.*;

public class Router {
    private final Routes routes;

    public Router(Routes routes) {
        this.routes = routes;
    }

    public Response respond(Request request) {
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

        return new ResponseBuilder()
                .addStatus(code)
                .addHeader("Content-Type", "text/html")
                .addBody(body)
                .build();
    }
}
