package com.saralein.server.middleware.verifier;

import com.saralein.server.filesystem.FilePath;
import com.saralein.server.filesystem.Resource;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import java.nio.file.Path;

public class PatchVerifier implements Verifier {
    private final Resource file;
    private final FilePath filePath;

    public PatchVerifier(Resource file, FilePath filePath) {
        this.file = file;
        this.filePath = filePath;
    }

    @Override
    public boolean validForMiddleware(Request request) {
        Path resource = filePath.absolute(request.getUri());
        return file.exists(resource) && isPatch(request);
    }

    private boolean isPatch(Request request) {
        String patch = Methods.PATCH.name();
        return request.getMethod().equals(patch);
    }
}
