package com.saralein.server.request.parser;

import com.saralein.server.request.transfer.RequestLine;

import java.util.List;

public class RequestLineParser {
    public RequestLine parse(List<String> request) throws Exception {
        String rawRequestLine = request.get(0);
        String[] requestLine = rawRequestLine.split(" ");

        if (!hasValidLength(requestLine)) {
            throw new Exception("Bad request. Connection closed.");
        }

        String method = parseMethod(requestLine);
        String uri = parseUri(requestLine);

        return new RequestLine(method, uri);
    }

    private boolean hasValidLength(String[] requestLine) {
        return requestLine.length == 3;
    }

    private String parseMethod(String[] requestLine) {
        return requestLine[0];
    }

    private String parseUri(String[] requestLine) {
        return requestLine[1];
    }
}
