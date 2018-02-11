package com.saralein.server.handler;

import com.saralein.server.FileHelper;
import com.saralein.server.filesystem.IO;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileHandler implements Handler {
    private final FileHelper fileHelper;
    private final IO fileIO;

    public FileHandler(FileHelper fileHelper, IO fileIO) {
        this.fileHelper = fileHelper;
        this.fileIO = fileIO;
    }

    @Override
    public Response handle(Request request) throws IOException, NoSuchAlgorithmException {
        String method = request.getMethod();

        if (isHeadRequest(method)) {
            return getHeadResponse(request);
        }

        if (isPatchRequest(method)) {
            return getPatchResponse(request);
        }

        return getGetResponse(request);
    }

    private Response getGetResponse(Request request) throws IOException {
        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", getMimeType(request))
                .body(createBody(request))
                .build();
    }

    private Response getHeadResponse(Request request) {
        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", getMimeType(request))
                .build();
    }

    private Response getPatchResponse(Request request) throws IOException, NoSuchAlgorithmException {
        String requestEtag = request.getHeader("If-Match");
        byte[] body = createBody(request);
        String resourceEtag = generateETag(body);

        if (requestEtag.equals(resourceEtag)) {
            return getPatchSuccessResponse(request);
        }

        return getPatchFailResponse();
    }

    private Response getPatchSuccessResponse(Request request) throws IOException {
        Path resource = fileHelper.createAbsolutePath(request.getUri());
        fileIO.write(resource, request.getBody());

        return new Response.Builder()
                .status(204)
                .addHeader("Content-Location", request.getUri())
                .build();
    }

    private Response getPatchFailResponse() {
        return new ErrorResponse(409).respond();
    }

    private boolean isHeadRequest(String method) {
        return method.equals(Methods.HEAD.name());
    }

    private boolean isPatchRequest(String method) {
        return method.equals(Methods.PATCH.name());
    }

    private byte[] createBody(Request request) throws IOException {
        Path resource = fileHelper.createAbsolutePath(request.getUri());

        return fileIO.read(resource);
    }

    private String generateETag(byte[] body) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");

        return DatatypeConverter.printHexBinary(sha1.digest(body)).toLowerCase();
    }

    private String getMimeType(Request request) {
        return fileHelper.determineMimeType(request.getUri());
    }
}
