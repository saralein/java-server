package com.saralein.server.router;

import com.saralein.server.middleware.Callable;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;

public class Router implements Callable {
    private final Routes routes;

    public Router(Routes routes) {
        this.routes = routes;
    }

    @Override
    public Response call(Request request) {
        String uri = request.getUri();
        String method = request.getMethod();

        if (routes.matchesRouteAndMethod(uri, method)) {
            Callable controller = routes.retrieveController(uri, method);
            return controller.call(request);
        } else if (routes.matchesRouteButNotMethod(uri, method)) {
            return new ErrorResponse(405).respond();
        } else {
            return new ErrorResponse(404).respond();
        }
    }
}
