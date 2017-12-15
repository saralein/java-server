package com.saralein.cobspec.router;

import com.saralein.server.controller.Controller;
import com.saralein.cobspec.controller.DirectoryController;
import com.saralein.cobspec.controller.FileController;
import com.saralein.cobspec.controller.ClientErrorController;
import com.saralein.server.router.Routes;
import java.util.HashMap;

public class RoutesBuilder {
    private HashMap<String, HashMap<String, Controller>> routes;
    private final DirectoryController directoryController;
    private final FileController fileController;
    private final ClientErrorController clientErrorController;

    public RoutesBuilder(DirectoryController directoryController,
                         FileController fileController,
                         ClientErrorController clientErrorController) {
        this.routes = new HashMap<>();
        this.directoryController = directoryController;
        this.fileController = fileController;
        this.clientErrorController = clientErrorController;
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
        return new Routes(routes, directoryController, fileController, clientErrorController);
    }
}
