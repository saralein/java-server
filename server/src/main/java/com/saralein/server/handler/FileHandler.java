package com.saralein.server.handler;

import com.saralein.server.FileHelper;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler implements Handler {
    private final FileHelper fileHelper;

    public FileHandler(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    @Override
    public Response handle(Request request) throws IOException {
        String requestMethod = request.getMethod();
        byte[] body = requestMethod.equals(Methods.GET.name()) ? createBody(request) : new byte[]{};

        return new Response.Builder()
                    .status(200)
                    .addHeader("Content-Type", getMimeType(request))
                    .body(body)
                    .build();
    }

    private byte[] createBody(Request request) throws IOException {
        Path resource = fileHelper.createAbsolutePath(request.getUri());

        return Files.readAllBytes(resource);
    }

    private String getMimeType(Request request) {
        return fileHelper.determineMimeType(request.getUri());
    }
}
