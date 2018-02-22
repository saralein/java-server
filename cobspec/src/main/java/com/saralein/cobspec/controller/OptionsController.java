package com.saralein.cobspec.controller;

import com.saralein.server.callable.Callable;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class OptionsController implements Callable {
    private final String methods;

    public OptionsController(String methods) {
        this.methods = methods;
    }

    public Response call(Request request) {
        return new Response.Builder()
                    .status(200)
                    .addHeader("Allow", methods)
                    .build();
    }
}
