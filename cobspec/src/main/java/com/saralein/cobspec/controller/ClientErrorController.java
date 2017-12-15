package com.saralein.cobspec.controller;

import com.saralein.server.controller.ErrorController;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;
import java.util.HashMap;

public class ClientErrorController implements ErrorController {
    private final HashMap<Integer, String> errorMessages;
    private int status;

    public ClientErrorController(HashMap<Integer, String> errorMessages) {
        this.errorMessages = errorMessages;
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
        return errorMessages.getOrDefault(status, "");
    }
}