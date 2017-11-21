package com.saralein.server.router;

import com.saralein.server.Controller.DirectoryController;
import com.saralein.server.Controller.FileController;
import com.saralein.server.Controller.NotFoundResponse;
import com.saralein.server.request.Request;
import com.saralein.server.response.*;
import java.io.File;

public class ServerRouter implements Router {
    private final FileHelper fileHelper;
    private final Routes routes;
    private File resource = null;
    private boolean resourceExists = false;
    private boolean resourceIsDirectory = false;

    public ServerRouter(Routes routes, FileHelper fileHelper) {
        this.routes = routes;
        this.fileHelper = fileHelper;
    }

    public byte[] resolveRequest(Request request) {
        resourceStatus(request);
        return route(request);
    }

    private void resourceStatus(Request request) {
        String resourceUri = fileHelper.createAbsolutePath(request.getUri());

        resource = new File(resourceUri);

        resourceExists = resource.exists();
        resourceIsDirectory = resource.isDirectory();
    }

    private byte[] route(Request request) {
        if (routes.isRoute(request.getUri())) {
            return routes.getController(request.getUri()).createResponse();
        } else if (!resourceExists) {
            return new NotFoundResponse().createResponse();
        } else if (resourceIsDirectory) {
            return new DirectoryController(resource, fileHelper).createResponse();
        } else {
            return new FileController(request, resource, fileHelper).createResponse();
        }
    }
}
