package com.saralein.server.router;

import com.saralein.server.controller.Controller;
import com.saralein.server.protocol.Methods;
import java.util.HashMap;

public class Routes {
    private final HashMap<String, HashMap<Methods, Controller>> routes;

    public Routes() {
        this.routes = new HashMap<>();
    }

    public Routes get(String uri, Controller controller) {
        addRoute(uri, Methods.GET, controller);
        return this;
    }

    public Routes post(String uri, Controller controller) {
        addRoute(uri, Methods.POST, controller);
        return this;
    }

    public Routes put(String uri, Controller controller) {
        addRoute(uri, Methods.PUT, controller);
        return this;
    }

    public Routes head(String uri, Controller controller) {
        addRoute(uri, Methods.HEAD, controller);
        return this;
    }

    public Routes options(String uri, Controller controller) {
        addRoute(uri, Methods.OPTIONS, controller);
        return this;
    }

    public Routes delete(String uri, Controller controller) {
        addRoute(uri, Methods.DELETE, controller);
        return this;
    }

    boolean matchesRouteAndMethod(String route, String method) {
        return isRoute(route) && hasMethod(route, method);
    }

    boolean matchesRouteButNotMethod(String route, String method) {
        return isRoute(route) && !hasMethod(route, method);
    }

    Controller retrieveController(String route, String method) {
        return routes.get(route).get(Methods.valueOf(method));
    }

    private void addRoute(String uri, Methods method, Controller controller) {
        if (!routes.containsKey(uri)) {
            routes.put(uri, new HashMap<Methods, Controller>(){{
                put(method, controller);
            }});
        } else {
            routes.get(uri).put(method, controller);
        }
    }

    private boolean hasMethod(String route, String method) {
        return routes.get(route).containsKey(Methods.valueOf(method));
    }

    private boolean isRoute(String route) {
        return routes.containsKey(route);
    }
}
