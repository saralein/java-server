package com.saralein.core.router;

import com.saralein.core.controller.Controller;
import com.saralein.core.request.Request;
import com.saralein.core.response.*;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerRouter implements Router {
    private final Routes routes;
    private final Path root;
    private boolean resourceExists = false;
    private boolean resourceIsDirectory = false;

    public ServerRouter(Routes routes, Path root) {
        this.routes = routes;
        this.root = root;
    }

    public Response resolveRequest(Request request) {
        resourceStatus(request);
        Controller controller = route(request);

        return controller.createResponse(request);
    }

    private void resourceStatus(Request request) {
        String separator = FileSystems.getDefault().getSeparator();
        String resourceUri = root.toAbsolutePath() + separator + request.getUri();
        Path resource = Paths.get(resourceUri);
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
