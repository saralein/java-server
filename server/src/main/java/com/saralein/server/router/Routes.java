package com.saralein.server.router;

import com.saralein.server.controller.Controller;
import com.saralein.server.protocol.Methods;
import java.util.HashMap;
import java.util.Map;

public class Routes {
    private final Map<String, HashMap<Methods, Controller>> routes;
    private final Map<String, RouteConfig> configuration;

    public Routes() {
        this.routes = new HashMap<>();
        this.configuration = new HashMap<>();
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

    public Routes use(String uri, RouteConfig routeConfig) {
        configuration.put(uri, routeConfig);
        return this;
    }

    public RouteConfig getConfig(String uri) {
        return configuration.getOrDefault(uri, new RouteConfig());
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
