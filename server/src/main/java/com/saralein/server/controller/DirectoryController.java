package com.saralein.server.controller;

import com.saralein.server.FileHelper;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryController implements Controller {
    private final FileHelper fileHelper;

    public DirectoryController(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    public Response respond(Request request) {
        String requestMethod = request.getMethod();
        String body = requestMethod.equals(Methods.GET.name()) ? createBody(request) : "";

        return new Response.Builder()
                    .status(200)
                    .addHeader("Content-Type", "text/html")
                    .body(body)
                    .build();
    }

    private String createBody(Request request) {
        StringBuilder filesHTML = new StringBuilder();
        Path resource = fileHelper.createAbsolutePath(request.getUri());

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
