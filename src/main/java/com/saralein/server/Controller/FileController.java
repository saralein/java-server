package com.saralein.server.Controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.FileHelper;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileController implements Controller {
    private final Request request;
    private final Path resource;
    private final FileHelper fileHelper;

    public FileController(Request request, Path resource, FileHelper fileHelper) {
        this.request = request;
        this.resource = resource;
        this.fileHelper = fileHelper;
    }

    public Response createResponse() {
        return new ResponseBuilder()
                    .addStatus(200)
                    .addHeader("Content-Type", getMimeType())
                    .addBody(createBody())
                    .build();
    }

    private byte[] createBody() {
        byte[] fileByte = new byte[]{};

        try {
            fileByte = Files.readAllBytes(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileByte;
    }

    private String getMimeType() {
        return fileHelper.determineMimeType(request.getUri());
    }
}
