package com.saralein.server.response;

import com.saralein.server.protocol.StatusCodes;

public class ErrorResponse {
    private final int status;

    public ErrorResponse(int status) {
        this.status = status;
    }

    public Response respond() {
        return getBaseBuilder()
                .build();
    }

    public Response respond(String headerName, String headerValue) {
        return getBaseBuilder()
                .addHeader(headerName, headerValue)
                .build();
    }

    private Response.Builder getBaseBuilder() {
        return new Response.Builder()
                .status(status)
                .addHeader("Content-Type", "text/html")
                .body(StatusCodes.retrieve(status));
    }
}
