package com.saralein.server.router;

import com.saralein.server.Controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerRouter implements Router {
    private final FileHelper fileHelper;
    private final Routes routes;
    private Path resource = null;
    private boolean resourceExists = false;
    private boolean resourceIsDirectory = false;

    public ServerRouter(Routes routes, FileHelper fileHelper) {
        this.routes = routes;
        this.fileHelper = fileHelper;
    }

    public Response resolveRequest(Request request) {
        resourceStatus(request);
        Controller controller = route(request);

        return controller.createResponse(request);
    }

    private void resourceStatus(Request request) {
        String resourceUri = fileHelper.createAbsolutePath(request.getUri());
        resource = Paths.get(resourceUri);
        resourceExists = Files.exists(resource);
        resourceIsDirectory = Files.isDirectory(resource);
    }

    private Controller route(Request request) {
        String uri = request.getUri();
        String method = request.getMethod();

        if (routes.isRoute(uri, method)) {
            return routes.retrieveController(uri, method);
        } else if (!resourceExists) {
            return routes.retrieve404Controller();
        } else if (resourceIsDirectory) {
            return routes.retrieveDirectoryController();
        } else {
            return routes.retrieveFileController();
        }
    }
}
