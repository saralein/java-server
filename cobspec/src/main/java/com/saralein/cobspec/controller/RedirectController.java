package com.saralein.cobspec.controller;

import com.saralein.server.middleware.Callable;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class RedirectController implements Callable {
    public Response call(Request request) {
        return new Response.Builder()
                    .status(302)
                    .addHeader("Location", "/")
                    .build();
    }
}
