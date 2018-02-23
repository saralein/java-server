package com.saralein.server.middleware;

import com.saralein.server.callable.Callable;
import com.saralein.server.handler.Handler;
import com.saralein.server.middleware.verifier.Verifier;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;

public class ResourceMiddleware implements Middleware {
    private final Handler handler;
    private final Verifier verifier;
    private Callable next;

    public ResourceMiddleware(Handler handler, Verifier verifier) {
        this.handler = handler;
        this.verifier = verifier;
        this.next = null;
    }

    @Override
    public Middleware apply(Callable callable) {
        this.next = callable;
        return this;
    }

    @Override
    public Response call(Request request) {
        if (verifier.validForMiddleware(request)) {
            return getResponse(request);
        } else {
            return next.call(request);
        }
    }

    private Response getResponse(Request request) {
        try {
            return handler.handle(request);
        } catch (Exception e) {
            return new ErrorResponse(500).respond();
        }
    }
}
