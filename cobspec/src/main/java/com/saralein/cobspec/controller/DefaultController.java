package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class DefaultController implements Controller {
    @Override
    public Response respond(Request request) {
        return new Response.Builder()
                    .status(200)
                    .build();
    }
}
