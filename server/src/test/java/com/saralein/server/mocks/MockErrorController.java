package com.saralein.server.mocks;

import com.saralein.server.controller.ErrorController;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;

public class MockErrorController implements ErrorController {
    private int status;
    private String body;

    public MockErrorController(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public Response createResponse(Request request) {
        return new ResponseBuilder()
                .addStatus(status)
                .addBody(body)
                .build();
    }

    public String getBody() {
        return body;
    }

    public ErrorController updateStatus(int status) {
        this.status = status;
        return this;
    }
}