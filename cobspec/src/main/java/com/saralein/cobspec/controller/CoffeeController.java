package com.saralein.cobspec.controller;

import com.saralein.server.callable.Callable;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;

public class CoffeeController implements Callable {
    public Response call(Request request) {
        return new ErrorResponse(418).respond();
    }
}
