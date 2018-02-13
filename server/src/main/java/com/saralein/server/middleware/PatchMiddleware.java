package com.saralein.server.middleware;

import com.saralein.server.FileHelper;
import com.saralein.server.filesystem.FileIO;
import com.saralein.server.handler.Handler;
import com.saralein.server.handler.PatchHandler;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

public class PatchMiddleware implements Middleware {
    private final FileHelper fileHelper;
    private final Handler patchHandler;
    private Callable next;

    public PatchMiddleware(FileHelper fileHelper, FileIO fileIO, MessageDigest messageDigest) {
        this(fileHelper, new PatchHandler(messageDigest, fileHelper, fileIO));
    }

    public PatchMiddleware(FileHelper fileHelper, Handler patchHandler) {
        this.fileHelper = fileHelper;
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
        if (isPatch(request) && fileExists(request)) {
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

    private boolean fileExists(Request request) {
        Path path = fileHelper.createAbsolutePath(request.getUri());
        return Files.exists(path) && !Files.isDirectory(path);
    }
}
