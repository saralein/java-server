package com.saralein.server.Controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.FileHelper;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileController implements Controller {
    private final FileHelper fileHelper;

    public FileController(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    public Response createResponse(Request request) {
        return new ResponseBuilder()
                    .addStatus(200)
                    .addHeader("Content-Type", getMimeType(request))
                    .addBody(createBody(request))
                    .build();
    }

    private byte[] createBody(Request request) {
        String resourceUri = fileHelper.createAbsolutePath(request.getUri());
        Path resource = Paths.get(resourceUri);

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