package com.saralein.server.response;

import com.saralein.server.protocol.StatusCodes;

public class ErrorResponse {
    private final int status;

    public ErrorResponse(int status) {
        this.status = status;
    }

    public Response respond() {
        return getBaseBuilder().build();
    }

    public Response respond(String name, String value) {
        return getBaseBuilder()
                .addHeader(name, value)
                .build();
    }

    private Response.Builder getBaseBuilder() {
        return new Response.Builder()
                .status(status)
                .addHeader("Content-Type", "text/html")
                .body(StatusCodes.retrieve(status));
    }
}
