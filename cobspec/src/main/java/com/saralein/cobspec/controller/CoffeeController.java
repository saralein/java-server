package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class CoffeeController implements Controller {
    public Response call(Request request) {
        return new Response.Builder()
                    .status(418)
                    .addHeader("Content-type", "text/html")
                    .body("I'm a teapot.")
                    .build();
    }
}
