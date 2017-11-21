package com.saralein.server.Controller;

import com.saralein.server.response.ResponseBuilder;

public class NotFoundResponse implements Controller {
    private final String contentType = "text/html";
    private final String body = "<center><h1>404</h1>Page not found.</center>";

    public byte[] createResponse() {
        return new ResponseBuilder()
                    .addStatus(404)
                    .addHeader("Content-Type", contentType)
                    .addBody(body)
                    .build()
                    .convertToBytes();
    }
}