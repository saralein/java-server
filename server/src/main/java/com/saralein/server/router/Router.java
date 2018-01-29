package com.saralein.server.router;

import com.saralein.server.controller.Controller;
import com.saralein.server.controller.ErrorController;
import com.saralein.server.middleware.Callable;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class Router implements Callable {
    private final Routes routes;
    private final ErrorController errorController;

    public Router(Routes routes) {
        this(routes, new ErrorController());
    }

    public Router(Routes routes, ErrorController errorController) {
        this.routes = routes;
        this.errorController = errorController;
    }

    public Response call(Request request) {
        String uri = request.getUri();
        String method = request.getMethod();

        if (routes.matchesRouteAndMethod(uri, method)) {
            Controller controller = routes.retrieveController(uri, method);
            return controller.respond(request);
        } else if (routes.matchesRouteButNotMethod(uri, method)) {
            return routeError(405, request);
        } else {
            return routeError(404, request);
        }
    }

    private Response routeError(int code, Request request) {
        return errorController
                .updateStatus(code)
                .respond(request);
    }
}
