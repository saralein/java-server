package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class OptionsController implements Controller {
    private final String methods;

    public OptionsController(String methods) {
        this.methods = methods;
    }

    public Response createResponse(Request request) {
        return new Response.Builder()
                    .addStatus(200)
                    .addHeader("Allow", methods)
                    .build();
    }
}
