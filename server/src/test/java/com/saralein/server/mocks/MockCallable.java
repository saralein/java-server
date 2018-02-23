package com.saralein.server.mocks;

import com.saralein.server.callable.Callable;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class MockCallable implements Callable {
    private boolean wasCalled;

    public MockCallable() {
        this.wasCalled = false;
    }

    public Response call(Request request) {
        wasCalled = true;
        return new Response.Builder()
                .status(200)
                .body("Callable response")
                .build();
    }

    public boolean wasCalled() {
        return wasCalled;
    }
}
