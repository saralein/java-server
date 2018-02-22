package com.saralein.server.mocks;

import com.saralein.server.middleware.Callable;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class MockRedirect implements Callable {
    public Response call(Request request) {
        return new Response.Builder()
                .status(302)
                .addHeader("Location", "/")
                .build();
    }
}
