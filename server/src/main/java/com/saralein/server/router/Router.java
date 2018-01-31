package com.saralein.server.router;

import com.saralein.server.controller.Controller;
import com.saralein.server.controller.ErrorController;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Router {
    private final Routes routes;
    private final Path root;
    private final Controller directoryController;
    private final Controller fileController;
    private final ErrorController errorController;
    private boolean resourceExists = false;
    private boolean resourceIsDirectory = false;

    public Router(
            Controller directoryController,
            Controller fileController,
            ErrorController errorController,
            Routes routes, Path root) {
        this.directoryController = directoryController;
        this.fileController = fileController;
        this.errorController = errorController;
        this.routes = routes;
        this.root = root;
    }

    public Response resolveRequest(Request request) {
        resourceStatus(request);
        Controller controller = resourceExists ? routeResource(request) : routeUri(request);

        return controller.respond(request);
    }

    private void resourceStatus(Request request) {
        String separator = FileSystems.getDefault().getSeparator();
        String resourceUri = root.toAbsolutePath() + separator + request.getUri();
        Path resource = Paths.get(resourceUri);
        resourceExists = Files.exists(resource);
        resourceIsDirectory = Files.isDirectory(resource);
    }

    private Controller routeResource(Request request) {
        String requestMethod = request.getMethod();
        String allowedMethods = Methods.allowedFileSystemMethods();
        boolean isAllowedMethod = allowedMethods.contains(requestMethod);

        if (resourceIsDirectory && isAllowedMethod) {
            return directoryController;
        } else if (isAllowedMethod) {
            return fileController;
        } else {
            return errorController.updateStatus(405);
        }
    }

    private Controller routeUri(Request request) {
        String uri = request.getUri();
        String method = request.getMethod();

        if (routes.matchesRouteAndMethod(uri, method)) {
            return routes.retrieveController(uri, method);
        } else if (routes.matchesRouteButNotMethod(uri, method)) {
            return errorController.updateStatus(405);
        } else {
            return errorController.updateStatus(404);
        }
    }
}
