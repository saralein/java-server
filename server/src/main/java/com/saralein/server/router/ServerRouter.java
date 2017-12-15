package com.saralein.server.router;

import com.saralein.server.controller.Controller;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.*;
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
        Controller controller = resourceExists ? routeResource(request) : routeUri(request);

        return controller.createResponse(request);
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
            return routes.retrieveDirectoryController();
        } else if (isAllowedMethod) {
            return routes.retrieveFileController();
        } else {
            return routes.retrieveErrorController(405);
        }
    }

    private Controller routeUri(Request request) {
        String uri = request.getUri();
        String method = request.getMethod();

        if (routes.matchesRouteAndMethod(uri, method)) {
            return routes.retrieveController(uri, method);
        } else if (routes.matchesRouteButNotMethod(uri, method)) {
            return routes.retrieveErrorController(405);
        } else {
            return routes.retrieveErrorController(404);
        }
    }
}
