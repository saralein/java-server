package com.saralein.server.Controller;

import com.saralein.server.response.FileHelper;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;
import java.io.IOException;
import java.nio.file.Path;

public class DirectoryController implements Controller {
    private final String contentType = "text/html";
    private final Path resource;
    private final FileHelper fileHelper;

    public DirectoryController(Path resource, FileHelper fileHelper) {
        this.resource = resource;
        this.fileHelper = fileHelper;
    }

    public Response createResponse() {
        return new ResponseBuilder()
                    .addStatus(200)
                    .addHeader("Content-Type", contentType)
                    .addBody(createBody())
                    .build();
    }

    private String createBody() {
        StringBuilder filesHTML = new StringBuilder();

        try {
            for (String filename : fileHelper.listFileNames(resource)) {
                String filePath = fileHelper.createRelativeFilePath(filename, resource);
                filesHTML.append(createFileHTML(filePath, filename));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filesHTML.toString();
    }

    private String createFileHTML(String filePath, String filename) {
        return "<li><a href=" + filePath + ">" + filename + "</a></li>";
    }
}
