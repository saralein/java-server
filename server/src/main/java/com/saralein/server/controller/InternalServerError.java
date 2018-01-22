package com.saralein.server.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class InternalServerError implements Controller {
    @Override
    public Response respond(Request request) {
        return new Response.Builder()
                .status(500)
                .build();
    }
}
