package com.saralein.server.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class UnauthorizedController implements Controller {
    private final String realm;

    public UnauthorizedController(String realm) {
        this.realm = realm;
    }

    @Override
    public Response respond(Request request) {
        String basicRealm = String.format("Basic realm=\"%s\"", realm);

        return new Response.Builder()
                .status(401)
                .addHeader("WWW-Authenticate", basicRealm)
                .build();
    }
}
