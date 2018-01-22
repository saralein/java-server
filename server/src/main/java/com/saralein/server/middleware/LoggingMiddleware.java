package com.saralein.server.middleware;

import com.saralein.server.logger.Logger;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class LoggingMiddleware extends Middleware {
    private final Logger logger;

    public LoggingMiddleware(Logger logger) {
        super();
        this.logger = logger;
    }

    @Override
    public Response call(Request request) {
        logger.trace(request);
        return middleware.call(request);
    }
}
