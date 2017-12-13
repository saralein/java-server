package com.saralein.server.router;

import com.saralein.server.controller.Controller;
import com.saralein.server.controller.ErrorController;
import java.util.HashMap;

public class Routes {
    private final Controller directoryController;
    private final Controller fileController;
    private final ErrorController errorController;
    private HashMap<String, HashMap<String, Controller>> routes;

    public Routes(HashMap<String, HashMap<String, Controller>> routes,
                  Controller directoryController,
                  Controller fileController,
                  ErrorController errorController) {
        this.routes = routes;
        this.directoryController = directoryController;
        this.fileController = fileController;
        this.errorController = errorController;
    }

    public boolean matchesRouteAndMethod(String route, String method) {
        return isRoute(route) && hasMethod(route, method);
    }

    public boolean matchesRouteButNotMethod(String route, String method) {
        return isRoute(route) && !hasMethod(route, method);
    }

    Controller retrieveController(String route, String method) {
        return routes.get(route).get(method);
    }

    Controller retrieveDirectoryController() {
        return directoryController;
    }

    Controller retrieveFileController() {
        return fileController;
    }

    Controller retrieveErrorController(int status) {
        return errorController.updateStatus(status);
    }

    private boolean hasMethod(String route, String method) {
        return routes.get(route).containsKey(method);
    }

    private boolean isRoute(String route) {
        return routes.containsKey(route);
    }
}
