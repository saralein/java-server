package com.saralein.server.middleware;

import com.saralein.server.FileHelper;
import com.saralein.server.handler.DirectoryHandler;
import com.saralein.server.handler.FileHandler;
import com.saralein.server.handler.Handler;
import com.saralein.server.protocol.Methods;
import com.saralein.server.protocol.StatusCodes;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StaticMiddleware implements Middleware {
    private final FileHelper fileHelper;
    private final Handler directoryHandler;
    private final Handler fileHandler;
    private Callable next;

    public StaticMiddleware(Path root) {
        this(new FileHelper(root), new DirectoryHandler(new FileHelper(root)),
                new FileHandler(new FileHelper(root)));
    }

    public StaticMiddleware(
            FileHelper fileHelper,
            Handler directoryHandler,
            Handler fileHandler) {
        this.fileHelper = fileHelper;
        this.directoryHandler = directoryHandler;
        this.fileHandler = fileHandler;
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
            return getResponse(resourcePath, request);
        } else {
            return next.call(request);
        }
    }

    private Response getResponse(Path resource, Request request) {
        if (!isAcceptedMethod(request.getMethod())) {
            return accessNotAllowed();
        }

        if (isDirectory(resource)) {
            return getDirectoryResponse(request);
        }

        return getFileResponse(request);
    }

    private Response getDirectoryResponse(Request request) {
        try {
            return directoryHandler.handle(request);
        } catch (IOException e) {
            return serverError();
        }
    }

    private Response getFileResponse(Request request) {
        try {
            return fileHandler.handle(request);
        } catch (IOException e) {
            return serverError();
        }
    }

    private boolean isAcceptedMethod(String method) {
        String allowedMethods = Methods.allowedFileSystemMethods();
        return allowedMethods.contains(method);
    }

    private boolean resourceExists(Path resource) {
        return Files.exists(resource);
    }

    private boolean isDirectory(Path resource) {
        return Files.isDirectory(resource);
    }

    private Response serverError() {
        return new Response.Builder()
                .status(500)
                .addHeader("Content-Type", "text/html")
                .body(StatusCodes.retrieve(500))
                .build();
    }

    private Response accessNotAllowed() {
        return new Response.Builder()
                .status(405)
                .addHeader("Content-Type", "text/html")
                .body(StatusCodes.retrieve(405))
                .build();
    }
}