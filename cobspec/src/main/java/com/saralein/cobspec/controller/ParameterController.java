package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.parameters.ParameterDecoder;
import com.saralein.server.parameters.ParameterParser;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParameterController implements Controller {
    private final ParameterParser parser;
    private final ParameterDecoder decoder;

    public ParameterController(
            ParameterParser parameterParser,
            ParameterDecoder parameterDecoder) {
        this.parser = parameterParser;
        this.decoder = parameterDecoder;
    }

    @Override
    public Response respond(Request request) {
        Map<String, String> params = parser.parse(request.getUri());
        Map<String, String> decodedParameters = decoder.decode(params);
        String body = formatDecodedParametersToBody(decodedParameters);

        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/plain")
                .body(body)
                .build();
    }

    private String formatDecodedParametersToBody(Map<String, String> decodedVariables) {
        List<String> body = new ArrayList<>();

        for (String key : decodedVariables.keySet()) {
            String combinedKeyValue = key + " = " + decodedVariables.get(key);
            body.add(combinedKeyValue);
        }

        return String.join("\n", body);
    }
}
