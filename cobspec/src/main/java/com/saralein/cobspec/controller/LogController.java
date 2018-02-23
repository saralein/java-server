package com.saralein.cobspec.controller;

import com.saralein.cobspec.data.LogStore;
import com.saralein.server.callable.Callable;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class LogController implements Callable {
    private final LogStore logStore;

    public LogController(LogStore logStore) {
        this.logStore = logStore;
    }

    public Response call(Request request) {
        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/plain")
                .body(logStore.retrieveLog())
                .build();
    }
}
