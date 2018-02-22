package com.saralein.cobspec.controller;

import com.saralein.server.middleware.Callable;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class CoffeeController implements Callable {
    public Response call(Request request) {
        return new Response.Builder()
                    .status(418)
                    .addHeader("Content-type", "text/html")
                    .body("I'm a teapot.")
                    .build();
    }
}
