package com.saralein.server.controller;

import com.saralein.server.filesystem.FileIO;
import com.saralein.server.filesystem.FilesInfo;
import com.saralein.server.filesystem.ServerPaths;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.io.IOException;
import java.nio.file.Path;

public class FileController implements Controller {
    private final ServerPaths paths;
    private final FilesInfo files;
    private final FileIO fileIO;

    public FileController(ServerPaths paths, FilesInfo files, FileIO fileIO) {
        this.paths = paths;
        this.files = files;
        this.fileIO = fileIO;
    }

    @Override
    public Response respond(Request request) {
        String uri = request.getUri();

        if (headRequested(request)) {
            return buildHeadResponse(uri);
        }

        try {
            return buildGetResponse(uri);
        } catch (IOException e) {
            return new InternalServerError().respond(request);
        }
    }

    private boolean headRequested(Request request) {
        return request.getMethod().equals(Methods.HEAD.name());
    }

    private Response buildGetResponse(String uri) throws IOException {
        Path file = paths.createAbsolutePath(uri);

        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", files.determineMimeType(uri))
                .body(fileIO.readFullFile(file))
                .build();
    }

    private Response buildHeadResponse(String uri) {
        return new Response.Builder()
                .status(200)
                .addHeader("Accept-Ranges", "bytes")
                .addHeader("Content-Type", files.determineMimeType(uri))
                .build();
    }
}
