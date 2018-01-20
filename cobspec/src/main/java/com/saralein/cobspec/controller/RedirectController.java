package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class RedirectController implements Controller {
    @Override
    public Response respond(Request request) {
        return new Response.Builder()
                    .status(302)
                    .addHeader("Location", "/")
                    .build();
    }
}
