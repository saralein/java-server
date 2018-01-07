package com.saralein.server.middleware;

import com.saralein.server.controller.Controller;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StaticMiddleware implements Middleware {
    private Path root;
    private final Controller directoryController;
    private final Controller fileController;

    public StaticMiddleware(
            Path root,
            Controller directoryController,
            Controller fileController) {
        this.root = root;
        this.directoryController = directoryController;
        this.fileController = fileController;
    }

    public Controller use(Controller controller) {
        return (request) -> {
            if (resourceExists(request)) {
                return createResponse(request);
            } else {
                return controller.createResponse(request);
            }
        };
    }

    private Response createResponse(Request request) {
        boolean isDirectory = isDirectory(request);
        boolean isAcceptedMethod = isAcceptedMethod(request);

        if (isDirectory&& isAcceptedMethod) {
            return directoryController.createResponse(request);
        } else if (isAcceptedMethod) {
            return fileController.createResponse(request);
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
        return new ResponseBuilder()
                .addStatus(405)
                .addHeader("Content-Type", "text/html")
                .build();
    }
}
