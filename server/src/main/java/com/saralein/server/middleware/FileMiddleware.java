package com.saralein.server.middleware;

import com.saralein.server.filesystem.File;
import com.saralein.server.filesystem.FileIO;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.handler.FileHandler;
import com.saralein.server.handler.Handler;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import java.nio.file.Path;

public class FileMiddleware implements Middleware {
    private final File file;
    private final FilePath filePath;
    private final Handler fileHandler;
    private Callable next;

    public FileMiddleware(File file, FilePath filePath, FileIO fileIO) {
        this(file, filePath, new FileHandler(file, filePath, fileIO));
    }

    public FileMiddleware(File file, FilePath filePath, Handler fileHandler) {
        this.file = file;
        this.filePath = filePath;
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
        Path path = filePath.absolute(request.getUri());

        if (file.exists(path)) {
            return getFileResponse(request);
        } else {
            return next.call(request);
        }
    }

    private Response getFileResponse(Request request) {
        if (!isAcceptedMethod(request.getMethod())) {
            return accessNotAllowed();
        }

        try {
            return fileHandler.handle(request);
        } catch (Exception e) {
            return serverError();
        }
    }

    private boolean isAcceptedMethod(String method) {
        String allowedMethods = Methods.allowedFileMethods();
        return allowedMethods.contains(method);
    }

    private Response serverError() {
        return new ErrorResponse(500).respond();
    }

    private Response accessNotAllowed() {
        return new ErrorResponse(405).respond();
    }
}
