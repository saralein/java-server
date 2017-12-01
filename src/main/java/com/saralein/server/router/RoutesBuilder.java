package com.saralein.server.router;

import com.saralein.server.Controller.Controller;
import com.saralein.server.Controller.DirectoryController;
import com.saralein.server.Controller.FileController;
import com.saralein.server.Controller.NotFoundController;

import java.util.HashMap;

public class RoutesBuilder {
    private HashMap<String, HashMap<String, Controller>> routes;
    private final DirectoryController directoryController;
    private final FileController fileController;
    private final NotFoundController notFoundController;

    public RoutesBuilder(DirectoryController directoryController,
                         FileController fileController,
                         NotFoundController notFoundController) {
        this.routes = new HashMap<>();
        this.directoryController = directoryController;
        this.fileController = fileController;
        this.notFoundController = notFoundController;
    }

    public RoutesBuilder addRoute(String uri, String method, Controller controller) {
        if (!routes.containsKey(uri)) {
            routes.put(uri, new HashMap<String, Controller>(){{
                put(method, controller);
            }});
        } else {
            routes.get(uri).put(method, controller);
        }

        return this;
    }

    public Routes build() {
        return new Routes(routes, directoryController, fileController, notFoundController);
    }
}
