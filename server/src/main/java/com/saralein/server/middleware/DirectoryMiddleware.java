package com.saralein.server.middleware;

import com.saralein.server.filesystem.Directory;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.handler.DirectoryHandler;
import com.saralein.server.handler.Handler;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import java.nio.file.Path;

public class DirectoryMiddleware implements Middleware {
    private final Directory directory;
    private final FilePath filePath;
    private final Handler directoryHandler;
    private Callable next;

    public DirectoryMiddleware(Directory directory, FilePath filePath) {
        this(directory, filePath, new DirectoryHandler(directory, filePath));
    }

    public DirectoryMiddleware(Directory directory, FilePath filePath, Handler directoryHandler) {
        this.directory = directory;
        this.filePath = filePath;
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
        Path path = filePath.absolute(request.getUri());

        if (directory.exists(path)) {
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
}
