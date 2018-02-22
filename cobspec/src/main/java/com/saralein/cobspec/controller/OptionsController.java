package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class OptionsController implements Controller {
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
