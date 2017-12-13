package com.saralein.server.router;

import com.saralein.server.controller.Controller;
import java.util.HashMap;

public class Routes {
    private final Controller directoryController;
    private final Controller fileController;
    private final Controller notFoundController;
    private HashMap<String, HashMap<String, Controller>> routes;

    public Routes(HashMap<String, HashMap<String, Controller>> routes,
                  Controller directoryController,
                  Controller fileController,
                  Controller notFoundController) {
        this.routes = routes;
        this.directoryController = directoryController;
        this.fileController = fileController;
        this.notFoundController = notFoundController;
    }

    public boolean isRoute(String route, String method) {
        return routes.containsKey(route) && routes.get(route).containsKey(method);
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

    Controller retrieve404Controller() {
        return notFoundController;
    }
}
