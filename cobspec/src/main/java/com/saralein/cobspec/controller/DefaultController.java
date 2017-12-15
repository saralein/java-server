package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;

public class DefaultController implements Controller {
    public Response createResponse(Request request) {
        return new ResponseBuilder()
                    .addStatus(200)
                    .build();
    }
}
