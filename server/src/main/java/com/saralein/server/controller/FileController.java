package com.saralein.server.controller;

import com.saralein.server.FileHelper;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileController implements Controller {
    private final FileHelper fileHelper;
    private final Controller partialContentController;

    public FileController(FileHelper fileHelper, Controller partialContentController) {
        this.fileHelper = fileHelper;
        this.partialContentController = partialContentController;
    }

    public Response respond(Request request) {
        if (requestingPartialContent(request)) {
            return partialContentController.respond(request);
        }

        return getResponse(request);
    }

    private boolean requestingPartialContent(Request request) {
        return !request.getHeader("Range").isEmpty();
    }

    private Response getResponse(Request request) {
        String requestMethod = request.getMethod();
        byte[] body = requestMethod.equals(Methods.GET.name()) ? createBody(request) : new byte[]{};

        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", getMimeType(request))
                .body(body)
                .build();
    }

    private byte[] createBody(Request request) {
        Path resource = fileHelper.createAbsolutePath(request.getUri());

        byte[] fileByte = new byte[]{};

        try {
            fileByte = Files.readAllBytes(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileByte;
    }

    private String getMimeType(Request request) {
        return fileHelper.determineMimeType(request.getUri());
    }
}
