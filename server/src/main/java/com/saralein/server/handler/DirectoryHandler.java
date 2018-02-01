package com.saralein.server.handler;

import com.saralein.server.FileHelper;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryHandler implements Handler {
    private final FileHelper fileHelper;

    public DirectoryHandler(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    public Response handle(Request request) throws IOException {
        String body = "";

        if (isGetRequest(request.getMethod())) {
            body = createBody(request.getUri());
        }

        return new Response.Builder()
                    .status(200)
                    .addHeader("Content-Type", "text/html")
                    .body(body)
                    .build();
    }

    private boolean isGetRequest(String method) {
        return method.equals(Methods.GET.name());
    }

    private String createBody(String uri) throws IOException {
        StringBuilder filesHTML = new StringBuilder();
        String resourceUri = fileHelper.createAbsolutePath(uri);
        Path resource = Paths.get(resourceUri);

        for (String filename : fileHelper.listFileNames(resource)) {
            String filePath = fileHelper.createRelativeFilePath(filename, resource);
            filesHTML.append(createFileHTML(filePath, filename));
        }

        return filesHTML.toString();
    }

    private String createFileHTML(String filePath, String filename) {
        return "<li><a href=" + filePath + ">" + filename + "</a></li>";
    }
}
