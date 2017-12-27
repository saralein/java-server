package com.saralein.server.mocks;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;

public class MockController implements Controller {
    private int status;
    private String body;

    public MockController(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public Response createResponse(Request request) {
        return new ResponseBuilder()
                .addStatus(status)
                .addBody(body)
                .build();
    }
}
