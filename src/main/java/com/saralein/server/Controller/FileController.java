package com.saralein.server.Controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.FileHelper;
import com.saralein.server.response.ResponseBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileController implements Controller {
    private final Request request;
    private final File resource;
    private final FileHelper fileHelper;

    public FileController(Request request, File resource, FileHelper fileHelper) {
        this.request = request;
        this.resource = resource;
        this.fileHelper = fileHelper;
    }

    public byte[] createResponse() {
        return new ResponseBuilder()
                    .addStatus(200)
                    .addHeader("Content-Type", getMimeType())
                    .addBody(createBody())
                    .build()
                    .convertToBytes();
    }

    private byte[] createBody() {
        byte[] fileByte = new byte[]{};

        try {
            fileByte = Files.readAllBytes(resource.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileByte;
    }

    private String getMimeType() {
        return fileHelper.determineMimeType(request.getUri());
    }
}
