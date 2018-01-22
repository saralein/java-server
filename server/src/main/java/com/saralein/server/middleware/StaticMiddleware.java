package com.saralein.server.middleware;

import com.saralein.server.FileHelper;
import com.saralein.server.controller.Controller;
import com.saralein.server.controller.DirectoryController;
import com.saralein.server.controller.FileController;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.router.Router;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StaticMiddleware implements Caller {
    private final Path root;
    private final Router router;
    private final Controller directoryController;
    private final Controller fileController;

    public StaticMiddleware(Path root, Router router) {
        this(root, router, new DirectoryController(new FileHelper(root)), new FileController(new FileHelper(root)));
    }

    public StaticMiddleware(
            Path root,
            Router router,
            Controller directoryController,
            Controller fileController) {
        this.root = root;
        this.router = router;
        this.directoryController = directoryController;
        this.fileController = fileController;
    }

    @Override
    public Response call(Request request) {
        if (resourceExists(request)) {
            return staticResponse(request);
        } else {
            return router.respond(request);
        }
    }

    private Response staticResponse(Request request) {
        boolean isDirectory = isDirectory(request);
        boolean isAcceptedMethod = isAcceptedMethod(request);

        if (isDirectory&& isAcceptedMethod) {
            return directoryController.respond(request);
        } else if (isAcceptedMethod) {
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

    private boolean resourceExists(Request request) {
        String separator = FileSystems.getDefault().getSeparator();
        String resourceUri = root.toAbsolutePath() + separator + request.getUri();
        Path resource = Paths.get(resourceUri);
        return Files.exists(resource);
    }

    private boolean isDirectory(Request request) {
        String separator = FileSystems.getDefault().getSeparator();
        String resourceUri = root.toAbsolutePath() + separator + request.getUri();
        Path resource = Paths.get(resourceUri);
        return Files.isDirectory(resource);
    }

    private Response accessNotAllowed() {
        return new Response.Builder()
                .status(405)
                .addHeader("Content-Type", "text/html")
                .build();
    }
}
