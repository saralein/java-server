package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;

public class CoffeeController implements Controller {
    public Response respond(Request request) {
        return new ResponseBuilder()
                    .addStatus(418)
                    .addHeader("Content-type", "text/html")
                    .addBody("I'm a teapot.")
                    .build();
    }
}
