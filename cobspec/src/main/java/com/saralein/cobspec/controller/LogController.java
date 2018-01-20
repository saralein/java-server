package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.controller.InternalServerError;
import com.saralein.server.filesystem.FileIO;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.io.IOException;
import java.nio.file.Path;

public class LogController implements Controller {
    private final Path log;
    private final FileIO fileIO;

    public LogController(Path log, FileIO fileIO) {
        this.log = log;
        this.fileIO = fileIO;
    }

    @Override
    public Response respond(Request request) {
        try {
            return buildLogResponse();
        } catch (IOException e) {
            return new InternalServerError().respond(request);
        }
    }

    private Response buildLogResponse() throws IOException {
        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/html")
                .body(fileIO.readFullFile(log))
                .build();
    }
}
