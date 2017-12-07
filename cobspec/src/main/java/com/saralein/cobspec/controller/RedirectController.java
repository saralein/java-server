package com.saralein.cobspec.controller;

import com.saralein.core.controller.Controller;
import com.saralein.core.request.Request;
import com.saralein.core.response.Response;
import com.saralein.core.response.ResponseBuilder;

public class RedirectController implements Controller {
    public Response createResponse(Request request) {
        return new ResponseBuilder()
                    .addStatus(302)
                    .addHeader("Location", "/")
                    .build();
    }
}
