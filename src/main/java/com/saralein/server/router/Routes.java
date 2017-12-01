package com.saralein.server.router;

import com.saralein.server.Controller.Controller;
import com.saralein.server.Controller.DirectoryController;
import com.saralein.server.Controller.FileController;
import com.saralein.server.Controller.NotFoundController;

import java.util.HashMap;

public class Routes {
    private final DirectoryController directoryController;
    private final FileController fileController;
    private final NotFoundController notFoundController;
    private HashMap<String, HashMap<String, Controller>> routes;

    public Routes(HashMap<String, HashMap<String, Controller>> routes,
                  DirectoryController directoryController,
                  FileController fileController,
                  NotFoundController notFoundController) {
        this.routes = routes;
        this.directoryController = directoryController;
        this.fileController = fileController;
        this.notFoundController = notFoundController;
    }

    boolean isRoute(String route, String method) {
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
