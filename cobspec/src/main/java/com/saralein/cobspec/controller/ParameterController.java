package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParameterController implements Controller {
    @Override
    public Response respond(Request request) {
        Map<String, String> parameters = request.getParameters();
        String body = formatParametersToBody(parameters);

        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/plain")
                .body(body)
                .build();
    }

    private String formatParametersToBody(Map<String, String> decodedVariables) {
        List<String> body = new ArrayList<>();

        for (String key : decodedVariables.keySet()) {
            String combinedKeyValue = key + " = " + decodedVariables.get(key);
            body.add(combinedKeyValue);
        }

        return String.join("\n", body);
    }
}
