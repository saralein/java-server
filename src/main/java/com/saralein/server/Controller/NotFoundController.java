package com.saralein.server.Controller;

import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;

public class NotFoundController implements Controller {
    private final String contentType = "text/html";
    private final String body = "<center><h1>404</h1>Page not found.</center>";

    public Response createResponse() {
        return new ResponseBuilder()
                    .addStatus(404)
                    .addHeader("Content-Type", contentType)
                    .addBody(body)
                    .build();
    }
}