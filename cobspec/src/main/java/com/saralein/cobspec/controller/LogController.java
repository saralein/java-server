package com.saralein.cobspec.controller;

import com.saralein.cobspec.data.LogStore;
import com.saralein.server.authorization.Authorizer;
import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class LogController implements Controller {
    private final LogStore logStore;
    private final Authorizer authorizer;
    private final Controller unauthorizedController;

    public LogController(LogStore logStore, Authorizer authorizer, Controller unauthorizedController) {
        this.logStore = logStore;
        this.authorizer = authorizer;
        this.unauthorizedController = unauthorizedController;
    }

    public Response respond(Request request) {
        String encodedAuthorization = request.getHeader("Authorization");

        return authorizer.isAuthorized(encodedAuthorization)
                ? getResponse()
                : unauthorizedController.respond(request);
    }

    private Response getResponse() {
        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/plain")
                .body(logStore.retrieveLog())
                .build();
    }
}
