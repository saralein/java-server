package com.saralein.server;

import com.saralein.server.middleware.Callable;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class Application {
    private final Callable callable;

    public Application(Callable callable) {
        this.callable = callable;
    }

    public Response call(Request request) {
        return callable.call(request);
    }
}
