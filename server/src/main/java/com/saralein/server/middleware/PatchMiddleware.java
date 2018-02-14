package com.saralein.server.middleware;

import com.saralein.server.filesystem.File;
import com.saralein.server.filesystem.FileIO;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.handler.Handler;
import com.saralein.server.handler.PatchHandler;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import java.nio.file.Path;

public class PatchMiddleware implements Middleware {
    private final File file;
    private final FilePath filePath;
    private final Handler patchHandler;
    private Callable next;

    public PatchMiddleware(File file, FilePath filePath, FileIO fileIO) {
        this(file, filePath, new PatchHandler(file, filePath, fileIO));
    }

    public PatchMiddleware(File file, FilePath filePath, Handler patchHandler) {
        this.file = file;
        this.filePath = filePath;
        this.patchHandler = patchHandler;
        this.next = null;
    }

    @Override
    public Middleware apply(Callable callable) {
        next = callable;
        return this;
    }

    @Override
    public Response call(Request request) {
        Path path = filePath.absolute(request.getUri());

        if (isPatch(request) && file.exists(path)) {
            return getPatchResponse(request);
        }

        return next.call(request);
    }

    private Response getPatchResponse(Request request) {
        try {
            return patchHandler.handle(request);
        } catch (Exception e) {
            return new ErrorResponse(500).respond();
        }
    }

    private boolean isPatch(Request request) {
        String patch = Methods.PATCH.name();
        return request.getMethod().equals(patch);
    }
}
