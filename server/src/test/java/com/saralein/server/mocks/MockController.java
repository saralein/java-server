package com.saralein.server.mocks;

import com.saralein.server.callable.Callable;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class MockController implements Callable {
    private int status;
    private String body;

    public MockController(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public Response call(Request request) {
        return new Response.Builder()
                .status(status)
                .body(body)
                .build();
    }
}
