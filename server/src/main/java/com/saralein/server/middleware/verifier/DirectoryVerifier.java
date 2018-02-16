package com.saralein.server.middleware.verifier;

import com.saralein.server.filesystem.FilePath;
import com.saralein.server.filesystem.Resource;
import com.saralein.server.request.Request;
import java.nio.file.Path;

public class DirectoryVerifier implements Verifier {
    private Resource directory;
    private FilePath filePath;

    public DirectoryVerifier(Resource directory, FilePath filePath) {
        this.directory = directory;
        this.filePath = filePath;
    }

    @Override
    public boolean validForMiddleware(Request request) {
        Path resource = filePath.absolute(request.getUri());
        return directory.exists(resource);
    }
}
