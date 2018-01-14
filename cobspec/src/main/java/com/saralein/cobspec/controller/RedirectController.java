package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class RedirectController implements Controller {
    public Response createResponse(Request request) {
        return new Response.Builder()
                    .addStatus(302)
                    .addHeader("Location", "/")
                    .build();
    }
}
