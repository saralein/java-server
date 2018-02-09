package com.saralein.server.request.parser;

import com.saralein.server.exchange.RequestLine;

public class RequestLineParser {
    public RequestLine parse(String rawRequestLine) throws Exception {
        String[] requestLine = rawRequestLine.split(" ");

        if (!hasValidLength(requestLine)) {
            throw new Exception("Invalid request.");
        }

        String method = parseMethod(requestLine);
        String uri = parseUri(requestLine);
        String query = parseQuery(requestLine);

        return new RequestLine(method, uri, query);
    }

    private boolean hasValidLength(String[] requestLine) {
        return requestLine.length == 3;
    }

    private String parseMethod(String[] requestLine) {
        return requestLine[0];
    }

    private String parseUri(String[] requestLine) {
        String[] uriWithQuery = parseUriWithQuery(requestLine);
        return uriWithQuery[0];
    }

    private String parseQuery(String[] requestLine) {
        String[] uriWithQuery = parseUriWithQuery(requestLine);

        if (uriWithQuery.length == 2) {
            return uriWithQuery[1];
        }

        return "";
    }

    private String[] parseUriWithQuery(String[] requestLine) {
        String uriWithQuery = requestLine[1];
        return uriWithQuery.split("\\?");
    }
}
