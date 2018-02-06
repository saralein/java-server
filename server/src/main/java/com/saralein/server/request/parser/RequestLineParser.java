package com.saralein.server.request.parser;

import com.saralein.server.request.transfer.RequestLine;

public class RequestLineParser {
    public RequestLine parse(String rawRequestLine) throws Exception {
        String[] requestLine = rawRequestLine.split(" ");

        if (!hasValidLength(requestLine)) {
            throw new Exception("Invalid request.");
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
