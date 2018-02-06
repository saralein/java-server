package com.saralein.server.mocks;

import com.saralein.server.handler.Handler;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

import java.io.IOException;

public class MockHandler implements Handler {
    private int status;
    private String body;

    public MockHandler(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public Response handle(Request request) throws IOException {
        return new Response.Builder()
                .status(status)
                .body(body)
                .build();
    }
}