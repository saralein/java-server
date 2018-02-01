package com.saralein.server.middleware;

import com.saralein.server.protocol.StatusCodes;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class FinalCallable implements Callable {
    @Override
    public Response call(Request request) {
        return new Response.Builder()
                .status(404)
                .addHeader("Content-Type", "text/html")
                .body(StatusCodes.retrieve(404))
                .build();
    }
}
