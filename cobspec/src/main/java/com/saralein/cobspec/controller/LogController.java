package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LogController implements Controller {
    private final Path log;

    public LogController(Path log) {
        this.log = log;
    }

    public Response createResponse(Request request) {
        return new Response.Builder()
                .addStatus(200)
                .addBody(createBody())
                .build();
    }

    private byte[] createBody() {
        try {
            return Files.readAllBytes(log);
        } catch (IOException e) {
            return new byte[]{};
        }
    }
}
