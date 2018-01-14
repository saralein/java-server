package com.saralein.server.controller;

import com.saralein.server.protocol.StatusCodes;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;

public class ErrorController implements Controller {
    private int status;

    public ErrorController() {
        this.status = 404;
    }

    public Response createResponse(Request request) {
        return new ResponseBuilder()
                    .addStatus(status)
                    .addHeader("Content-Type", "text/html")
                    .addBody(selectBodyByError())
                    .build();
    }

    public ErrorController updateStatus(int status) {
        this.status = status;
        return this;
    }

    private String selectBodyByError() {
        return StatusCodes.retrieve(status);
    }
}