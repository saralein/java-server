package com.saralein.server.mocks;

import com.saralein.server.middleware.Caller;
import com.saralein.server.middleware.Middleware;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;

public class MockMiddleware extends Middleware implements Caller {
    private boolean wasCalled = false;

    @Override
    public Response call(Request request) {
        wasCalled = true;
        return new ResponseBuilder()
                .addBody("Middleware response")
                .build();
    }

    public boolean wasCalled() {
        return wasCalled;
    }
}
