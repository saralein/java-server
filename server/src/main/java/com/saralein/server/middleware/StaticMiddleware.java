package com.saralein.server.middleware;

import com.saralein.server.FileHelper;
import com.saralein.server.filesystem.FileIO;
import com.saralein.server.handler.DirectoryHandler;
import com.saralein.server.handler.FileHandler;
import com.saralein.server.handler.Handler;
import com.saralein.server.handler.PartialFileHandler;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticMiddleware implements Middleware {
    private final FileHelper fileHelper;
    private final Handler directoryHandler;
    private final Handler fileHandler;
    private final Handler partialFileHandler;
    private Callable next;

    public StaticMiddleware(Path root) {
        this(new FileHelper(root),
                new DirectoryHandler(new FileHelper(root)),
                new FileHandler(new FileHelper(root)),
                new PartialFileHandler(new FileHelper(root), new FileIO()));
    }

    public StaticMiddleware(
            FileHelper fileHelper,
            Handler directoryHandler,
            Handler fileHandler,
            Handler partialFileHandler) {
        this.fileHelper = fileHelper;
        this.directoryHandler = directoryHandler;
        this.fileHandler = fileHandler;
        this.partialFileHandler = partialFileHandler;
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

        if (requestingPartialFile(request)) {
            return getPartialFileResponse(request);
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

    private Response getPartialFileResponse(Request request) {
        try {
            return partialFileHandler.handle(request);
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

    private boolean requestingPartialFile(Request request) {
        return !request.getHeader("Range").isEmpty();
    }

    private Response serverError() {
        return new ErrorResponse(500).respond();
    }

    private Response accessNotAllowed() {
        return new ErrorResponse(405).respond();
    }
}
