package com.saralein.cobspec.controller;

import com.saralein.cobspec.data.LogStore;
import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class LogController implements Controller {
    private final LogStore logStore;

    public LogController(LogStore logStore) {
        this.logStore = logStore;
    }

    public Response respond(Request request) {
        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/plain")
                .body(logStore.retrieveLog())
                .build();
    }
}
