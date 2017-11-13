package com.saralein.server.router;

import com.saralein.server.request.Request;
import com.saralein.server.response.*;
import java.io.File;

public class ServerRouter implements Router {
    private final FileHelper fileHelper;
    private File resource = null;
    private boolean resourceExists = false;
    private boolean resourceIsDirectory = false;

    public ServerRouter(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    public byte[] getResponse(Request request) {
        resourceStatus(request);
        return route(request);
    }

    private void resourceStatus(Request request) {
        String resourceUri = fileHelper.getRelativePath(request.getUri());

        resource = new File(resourceUri);

        resourceExists = resource.exists();
        resourceIsDirectory = resource.isDirectory();
    }

    private byte[] route(Request request) {
        if (!resourceExists) {
            return new NotFoundResponse().createResponse();
        } else if (resourceIsDirectory) {
            return new DirectoryResponse(resource, fileHelper).createResponse();
        } else {
            return new FileResponse(request, resource, fileHelper).createResponse();
        }
    }
}
