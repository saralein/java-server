package com.saralein.server.middleware;

import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;

public class FinalCallable implements Callable {
    @Override
    public Response call(Request request) {
        return new ErrorResponse(404).respond();
    }
}
