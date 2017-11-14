package com.saralein.server.response;

import static com.saralein.server.Constants.STATUS_CODES;
import java.io.File;

public class DirectoryResponse implements Response {
    private final String contentType = "text/html";
    private final File resource;
    private final FileHelper fileHelper;

    public DirectoryResponse(File resource, FileHelper fileHelper) {
        this.resource = resource;
        this.fileHelper = fileHelper;
    }

    public byte[] createResponse() {
        StringBuilder response = new StringBuilder();
        response.append(createHeader());
        response.append(createBody());

        return response.toString().getBytes();
    }

    private String createHeader() {
        return new Header(STATUS_CODES.get(200), contentType).createContents();
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
