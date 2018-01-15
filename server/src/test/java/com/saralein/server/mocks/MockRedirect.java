package com.saralein.server.mocks;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;

public class MockRedirect implements Controller {
    public Response respond(Request request) {
        return new ResponseBuilder()
                .addStatus(302)
                .addHeader("Location", "/")
                .build();
    }
}