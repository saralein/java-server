package com.saralein.server.handler;

import com.saralein.server.filesystem.Directory;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.io.IOException;
import java.nio.file.Path;

public class DirectoryHandler implements Handler {
    private final Directory directory;
    private final FilePath filePath;

    public DirectoryHandler(Directory directory, FilePath filePath) {
        this.directory = directory;
        this.filePath = filePath;
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
        Path resource = filePath.absolute(uri);

        for (String filename : directory.listFileNames(resource)) {
            String relativePath = filePath.relative(filename, resource);
            filesHTML.append(createFileHTML(relativePath, filename));
        }

        return filesHTML.toString();
    }

    private String createFileHTML(String filePath, String filename) {
        return "<li><a href=" + filePath + ">" + filename + "</a></li>";
    }
}
