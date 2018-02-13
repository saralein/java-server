package com.saralein.server.handler;

import com.saralein.server.FileHelper;
import com.saralein.server.filesystem.IO;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Path;
import java.security.MessageDigest;

public class PatchHandler implements Handler {
    private final MessageDigest messageDigest;
    private final FileHelper fileHelper;
    private final IO fileIO;

    public PatchHandler(MessageDigest messageDigest, FileHelper fileHelper, IO fileIO) {
        this.messageDigest = messageDigest;
        this.fileHelper = fileHelper;
        this.fileIO = fileIO;
    }

    @Override
    public Response handle(Request request) throws IOException {
        String requestEtag = request.getHeader("If-Match");
        byte[] body = createBody(request);
        String resourceEtag = generateETag(body);

        if (requestEtag.equals(resourceEtag)) {
            return getSuccessResponse(request);
        }

        return getFailResponse();
    }

    private Response getSuccessResponse(Request request) throws IOException {
        Path resource = fileHelper.createAbsolutePath(request.getUri());
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
        Path resource = fileHelper.createAbsolutePath(request.getUri());

        return fileIO.read(resource);
    }

    private String generateETag(byte[] body) {
        return DatatypeConverter.printHexBinary(messageDigest.digest(body)).toLowerCase();
    }
}
