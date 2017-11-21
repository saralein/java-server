package com.saralein.server.Controller;

import com.saralein.server.response.FileHelper;
import com.saralein.server.response.ResponseBuilder;
import java.io.File;

public class DirectoryController implements Controller {
    private final String contentType = "text/html";
    private final File resource;
    private final FileHelper fileHelper;

    public DirectoryController(File resource, FileHelper fileHelper) {
        this.resource = resource;
        this.fileHelper = fileHelper;
    }

    public byte[] createResponse() {
        return new ResponseBuilder()
                    .addStatus(200)
                    .addHeader("Content-Type", contentType)
                    .addBody(createBody())
                    .build()
                    .convertToBytes();
    }

    private String createBody() {
        StringBuilder filesHTML = new StringBuilder();

        for (String filename: fileHelper.listFileNames(resource)) {
            String filepath = fileHelper.createRelativeFilePath(filename, resource);
            filesHTML.append(createFileHTML(filepath, filename));
        }

        return filesHTML.toString();
    }

    private String createFileHTML(String filepath, String filename) {
        return "<li><a href=" + filepath + ">" + filename + "</a></li>";
    }
}
