package com.saralein.cobspec.router;

import com.saralein.server.controller.Controller;
import com.saralein.cobspec.controller.DirectoryController;
import com.saralein.cobspec.controller.FileController;
import com.saralein.cobspec.controller.NotFoundController;
import com.saralein.server.router.Routes;
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
