package com.saralein.cobspec.controller;

import com.saralein.server.callable.Callable;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class DefaultController implements Callable {
    public Response call(Request request) {
        return new Response.Builder()
                    .status(200)
                    .build();
    }
}
