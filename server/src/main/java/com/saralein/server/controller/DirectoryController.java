package com.saralein.server.controller;

import com.saralein.server.filesystem.FilesInfo;
import com.saralein.server.filesystem.ServerPaths;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.io.IOException;
import java.nio.file.Path;

public class DirectoryController implements Controller {
    private final FilesInfo files;
    private final ServerPaths paths;

    public DirectoryController(ServerPaths paths, FilesInfo files) {
        this.paths = paths;
        this.files = files;
    }

    @Override
    public Response respond(Request request) {
        try {
            return buildResponse(request);
        } catch (IOException e) {
            return new InternalServerError().respond(request);
        }
    }

    private Response buildResponse(Request request) throws IOException {
        String requestMethod = request.getMethod();
        String body = requestMethod.equals(Methods.GET.name()) ? createBody(request) : "";

        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/html")
                .body(body)
                .build();
    }

    private String createBody(Request request) throws IOException {
        StringBuilder filesHTML = new StringBuilder();
        Path resource = paths.createAbsolutePath(request.getUri());

        for (String filename : files.listFileNames(resource)) {
            String filePath = paths.createRelativePathString(filename, resource);
            filesHTML.append(createFileHTML(filePath, filename));
        }

        return filesHTML.toString();
    }

    private String createFileHTML(String filePath, String filename) {
        return "<li><a href=" + filePath + ">" + filename + "</a></li>";
    }
}
