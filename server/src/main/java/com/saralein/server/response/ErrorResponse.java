package com.saralein.server.response;

import com.saralein.server.protocol.StatusCodes;

public class ErrorResponse {
    private final int status;

    public ErrorResponse(int status) {
        this.status = status;
    }

    public Response respond() {
        return new Response.Builder()
                .status(status)
                .addHeader("Content-Type", "text/html")
                .body(StatusCodes.retrieve(status))
                .build();
    }
}
