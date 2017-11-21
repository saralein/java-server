package com.saralein.server.router;

import com.saralein.server.Controller.Controller;

import java.util.HashMap;

public class Routes {
    private HashMap<String, Controller> routes;

    public Routes() {
        this.routes = new HashMap<>();
    }

    void addRoute(String route, Controller controller) {
        routes.put(route, controller);
    }

    boolean isRoute(String route) {
        return routes.containsKey(route);
    }

    Controller getController(String route) {
        return routes.get(route);
    }
}
