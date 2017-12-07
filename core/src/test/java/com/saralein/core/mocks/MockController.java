package com.saralein.core.mocks;

import com.saralein.core.controller.Controller;
import com.saralein.core.request.Request;
import com.saralein.core.response.Response;
import com.saralein.core.response.ResponseBuilder;

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

    public String getBody() {
        return body;
    }
}
