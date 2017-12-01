package com.saralein.server.Controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.FileHelper;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryController implements Controller {
    private final FileHelper fileHelper;

    public DirectoryController(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    public Response createResponse(Request request) {
        return new ResponseBuilder()
                    .addStatus(200)
                    .addHeader("Content-Type", "text/html")
                    .addBody(createBody(request))
                    .build();
    }

    private String createBody(Request request) {
        StringBuilder filesHTML = new StringBuilder();
        String resourceUri = fileHelper.createAbsolutePath(request.getUri());
        Path resource = Paths.get(resourceUri);

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
