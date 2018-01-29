package com.saralein.server.middleware;

import com.saralein.server.FileHelper;
import com.saralein.server.controller.Controller;
import com.saralein.server.controller.DirectoryController;
import com.saralein.server.controller.FileController;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StaticMiddleware implements Middleware {
    private final FileHelper fileHelper;
    private final Controller directoryController;
    private final Controller fileController;
    private Callable next;

    public StaticMiddleware(Path root) {
        this(new FileHelper(root), new DirectoryController(new FileHelper(root)),
                new FileController(new FileHelper(root)));
    }

    public StaticMiddleware(
            FileHelper fileHelper,
            Controller directoryController,
            Controller fileController) {
        this.fileHelper = fileHelper;
        this.directoryController = directoryController;
        this.fileController = fileController;
        this.next = null;
    }

    @Override
    public Middleware apply(Callable callable) {
        this.next = callable;
        return this;
    }

    @Override
    public Response call(Request request) {
        String resource = fileHelper.createAbsolutePath(request.getUri());
        Path resourcePath = Paths.get(resource);

        if (resourceExists(resourcePath)) {
            return staticResponse(resourcePath, request);
        } else {
            return next.call(request);
        }
    }

    private Response staticResponse(Path resource, Request request) {
        boolean isDirectory = isDirectory(resource);
        boolean isAcceptedMethod = isAcceptedMethod(request);

        if (isDirectory && isAcceptedMethod) {
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

    private boolean resourceExists(Path resource) {
        return Files.exists(resource);
    }

    private boolean isDirectory(Path resource) {
        return Files.isDirectory(resource);
    }

    private Response accessNotAllowed() {
        return new Response.Builder()
                .status(405)
                .addHeader("Content-Type", "text/html")
                .build();
    }
}