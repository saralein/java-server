package com.saralein.server.handler;

import com.saralein.server.filesystem.File;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.filesystem.IO;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import java.io.IOException;
import java.nio.file.Path;

public class PatchHandler implements Handler {
    private final File file;
    private final FilePath filePath;
    private final IO fileIO;

    public PatchHandler(File file, FilePath filePath, IO fileIO) {
        this.file = file;
        this.filePath = filePath;
        this.fileIO = fileIO;
    }

    @Override
    public Response handle(Request request) throws IOException {
        String requestEtag = request.getHeader("If-Match");
        byte[] body = createBody(request);
        String resourceEtag = file.computeHash(body);

        if (requestEtag.equals(resourceEtag)) {
            return getSuccessResponse(request);
        }

        return getFailResponse();
    }

    private Response getSuccessResponse(Request request) throws IOException {
        Path resource = filePath.absolute(request.getUri());
        fileIO.write(resource, request.getBody());

        return new Response.Builder()
                .status(204)
                .addHeader("Content-Location", request.getUri())
                .build();
    }

    private Response getFailResponse() {
        return new ErrorResponse(409).respond();
    }

    private byte[] createBody(Request request) throws IOException {
        Path resource = filePath.absolute(request.getUri());
        return fileIO.readAllBytes(resource);
    }
}
