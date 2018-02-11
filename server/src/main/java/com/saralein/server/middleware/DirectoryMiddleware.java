package com.saralein.server.middleware;

import com.saralein.server.FileHelper;
import com.saralein.server.handler.DirectoryHandler;
import com.saralein.server.handler.Handler;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryMiddleware implements Middleware {
    private final FileHelper fileHelper;
    private final Handler directoryHandler;
    private Callable next;

    public DirectoryMiddleware(FileHelper fileHelper) {
        this(fileHelper, new DirectoryHandler(fileHelper));
    }

    public DirectoryMiddleware(FileHelper fileHelper, Handler directoryHandler) {
        this.fileHelper = fileHelper;
        this.directoryHandler = directoryHandler;
        this.next = null;
    }

    @Override
    public Middleware apply(Callable callable) {
        next = callable;
        return this;
    }

    @Override
    public Response call(Request request) {
        if (directoryExists(request)) {
            return getDirectoryResponse(request);
        }

        return next.call(request);
    }

    private Response getDirectoryResponse(Request request) {
        try {
            return directoryHandler.handle(request);
        } catch (Exception e) {
            return new ErrorResponse(500).respond();
        }
    }

    private boolean directoryExists(Request request) {
        Path path = fileHelper.createAbsolutePath(request.getUri());
        return Files.exists(path) && Files.isDirectory(path);
    }
}
