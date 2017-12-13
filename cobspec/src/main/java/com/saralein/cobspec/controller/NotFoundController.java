package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;

public class NotFoundController implements Controller {
    public Response createResponse(Request request) {
        return new ResponseBuilder()
                    .addStatus(404)
                    .addHeader("Content-Type", "text/html")
                    .addBody("<center><h1>404</h1>Page not found.</center>")
                    .build();
    }
}