package com.saralein.server.middleware;

import com.saralein.server.filesystem.ServerFileIO;
import com.saralein.server.filesystem.ServerFiles;
import com.saralein.server.controller.Controller;
import com.saralein.server.controller.DirectoryController;
import com.saralein.server.controller.FileController;
import com.saralein.server.controller.PartialContentController;
import com.saralein.server.filesystem.ServerPaths;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.router.Router;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticMiddleware implements Caller {
    private final ServerPaths paths;
    private final Router router;
    private final Controller directoryController;
    private final Controller fileController;
    private final Controller partialContentController;

    public StaticMiddleware(Path root, Router router) {
        this(new ServerPaths(root), router,
                new DirectoryController(new ServerPaths(root), new ServerFiles()),
                new FileController(new ServerPaths(root), new ServerFiles(), new ServerFileIO()),
                new PartialContentController(new ServerPaths(root), new ServerFiles(), new ServerFileIO()));
    }

    public StaticMiddleware(
            ServerPaths paths,
            Router router,
            Controller directoryController,
            Controller fileController,
            Controller partialContentController
    ) {
        this.paths = paths;
        this.router = router;
        this.directoryController = directoryController;
        this.fileController = fileController;
        this.partialContentController = partialContentController;
    }

    @Override
    public Response call(Request request) {
        Path resource = paths.createAbsolutePath(request.getUri());

        if (resourceExists(resource)) {
            return staticResponse(resource, request);
        } else {
            return router.respond(request);
        }
    }

    private Response staticResponse(Path resource, Request request) {
        boolean isDirectory = isDirectory(resource);
        boolean isAcceptedMethod = isAcceptedMethod(request);

        if (isDirectory&& isAcceptedMethod) {
            return directoryController.respond(request);
        } else if (isAcceptedMethod) {
            if (requestingPartialContent(request)) {
                return partialContentController.respond(request);
            }
            return fileController.respond(request);
        } else {
            return accessNotAllowed();
        }
    }

    private boolean isAcceptedMethod(Request request) {
        String requestMethod = request.getMethod();
        String allowedMethods = Methods.allowedFileSystemMethods();
        return allowedMethods.contains(requestMethod);
    }

    private boolean resourceExists(Path resource) {
        return Files.exists(resource);
    }

    private boolean isDirectory(Path resource) {
        return Files.isDirectory(resource);
    }

    private boolean requestingPartialContent(Request request) {
        return !request.getHeader("Range").isEmpty();
    }

    private Response accessNotAllowed() {
        return new Response.Builder()
                .status(405)
                .addHeader("Content-Type", "text/html")
                .build();
    }
}
