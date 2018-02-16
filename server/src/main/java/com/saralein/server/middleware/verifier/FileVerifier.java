package com.saralein.server.middleware.verifier;

import com.saralein.server.filesystem.FilePath;
import com.saralein.server.filesystem.Resource;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import java.nio.file.Path;

public class FileVerifier implements Verifier {
    private final Resource file;
    private final FilePath filePath;

    public FileVerifier(Resource file, FilePath filePath) {
        this.file = file;
        this.filePath = filePath;
    }

    @Override
    public boolean validForMiddleware(Request request) {
        Path resource = filePath.absolute(request.getUri());
        return file.exists(resource) && isGetOrHead(request) && isNotPartialRequest(request);
    }

    private boolean isGetOrHead(Request request) {
        String method = request.getMethod();
        String get = Methods.GET.name();
        String head = Methods.HEAD.name();
        return method.equals(get) || method.equals(head);
    }

    private boolean isNotPartialRequest(Request request) {
        String range = request.getHeader("Range");
        return range.isEmpty();
    }
}
