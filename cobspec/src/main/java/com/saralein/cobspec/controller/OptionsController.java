package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;

public class OptionsController implements Controller {
    private final String methods;

    public OptionsController(String methods) {
        this.methods = methods;
    }

    public Response respond(Request request) {
        return new ResponseBuilder()
                    .addStatus(200)
                    .addHeader("Allow", methods)
                    .build();
    }
}
