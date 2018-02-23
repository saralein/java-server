package com.saralein.server.handler;

import com.saralein.server.filesystem.File;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.filesystem.IO;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.io.IOException;
import java.nio.file.Path;

public class FileHandler implements Handler {
    private final File file;
    private final FilePath filePath;
    private final IO fileIO;

    public FileHandler(File file, FilePath filePath, IO fileIO) {
        this.file = file;
        this.filePath = filePath;
        this.fileIO = fileIO;
    }

    @Override
    public Response handle(Request request) throws IOException {
        String method = request.getMethod();
        byte[] body = createBody(request);
        String etag = file.computeHash(body);

        if (method.equals(Methods.HEAD.name())) {
            return getHeadResponse(request, etag);
        }

        return getGetResponse(request, etag, body);
    }

    private Response getGetResponse(Request request, String etag, byte[] body) {
        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", getMimeType(request))
                .addHeader("ETag", etag)
                .body(body)
                .build();
    }

    private Response getHeadResponse(Request request, String etag) {
        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", getMimeType(request))
                .addHeader("ETag", etag)
                .build();
    }

    private byte[] createBody(Request request) throws IOException {
        Path resource = filePath.absolute(request.getUri());
        return fileIO.readAllBytes(resource);
    }

    private String getMimeType(Request request) {
        return file.mimeType(request.getUri());
    }
}
