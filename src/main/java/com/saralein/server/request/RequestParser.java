package com.saralein.server.request;

import static com.saralein.server.Constants.CRLF;

import java.util.HashMap;

public class RequestParser {
    private String[] split(String request, String splitter) {
        return request.split(splitter);
    }

    private String[] splitRequestLine(String[] fullRequest) {
        return split(fullRequest[0], " ");
    }

    private String setMethod(String[] requestLine) {
        return requestLine[0];
    }

    private String setUri(String[] requestLine) {
        return requestLine[1];
    }

    private String setVersion(String[] requestLine) {
        return requestLine[2];
    }

    private HashMap<String, String> createRequestMap(String[] requestLine) {
        HashMap<String, String> parsedRequest = new HashMap<>();

        parsedRequest.put("method", setMethod(requestLine));
        parsedRequest.put("uri", setUri(requestLine));
        parsedRequest.put("version", setVersion(requestLine));

        return parsedRequest;
    }

    public HashMap<String, String> parse(String request) {
        String[] fullRequest = split(request, CRLF);
        String[] requestLine = splitRequestLine(fullRequest);
        return createRequestMap(requestLine);
    }
}
