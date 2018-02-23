package com.saralein.server.middleware;

import com.saralein.server.callable.Callable;
import com.saralein.server.filesystem.File;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import java.nio.file.Path;

public class FileMethodMiddleware implements Middleware {
    private File file;
    private FilePath filePath;
    private Callable next;

    public FileMethodMiddleware(File file, FilePath filePath) {
        this.file = file;
        this.filePath = filePath;
        this.next = null;
    }

    @Override
    public Middleware apply(Callable callable) {
        this.next = callable;
        return this;
    }

    @Override
    public Response call(Request request) {
        Path resource = filePath.absolute(request.getUri());

        if (file.exists(resource) && isNotAcceptedMethod(request)) {
            return new ErrorResponse(405).respond("Allow", Methods.allowedFileMethods());
        }

        return next.call(request);
    }

    private boolean isNotAcceptedMethod(Request request) {
        String method = request.getMethod();
        String allowedMethods = Methods.allowedFileMethods();
        return !allowedMethods.contains(method);
    }
}
