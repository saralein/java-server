package com.saralein.server.middleware;

import com.saralein.server.FileHelper;
import com.saralein.server.filesystem.FileIO;
import com.saralein.server.handler.DirectoryHandler;
import com.saralein.server.handler.FileHandler;
import com.saralein.server.handler.Handler;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticMiddleware implements Middleware {
    private final FileHelper fileHelper;
    private final Handler directoryHandler;
    private final Handler fileHandler;
    private Callable next;

    public StaticMiddleware(Path root) {
        this(new FileHelper(root), new DirectoryHandler(new FileHelper(root)),
                new FileHandler(new FileHelper(root), new FileIO()));
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
        Path resource = fileHelper.createAbsolutePath(request.getUri());

        if (resourceExists(resource)) {
            return getResponse(resource, request);
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
        } catch (Exception e) {
            return serverError();
        }
    }

    private Response getFileResponse(Request request) {
        try {
            return fileHandler.handle(request);
        } catch (Exception e) {
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
        return new ErrorResponse(500).respond();
    }

    private Response accessNotAllowed() {
        return new ErrorResponse(405).respond();
    }
}