package com.saralein.server.middleware;

import com.saralein.server.callable.Callable;
import com.saralein.server.logger.Logger;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class LoggingMiddleware implements Middleware {
    private final Logger logger;
    private Callable next;

    public LoggingMiddleware(Logger logger) {
        this.logger = logger;
        this.next = null;
    }

    @Override
    public Middleware apply(Callable callable) {
        next = callable;
        return this;
    }

    @Override
    public Response call(Request request) {
        logger.trace(request.getRequestLine());
        return next.call(request);
    }
}
