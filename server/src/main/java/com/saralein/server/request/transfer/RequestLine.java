package com.saralein.server.request.transfer;

public class RequestLine {
    private final String method;
    private final String uri;
    private final String query;

    public RequestLine(String method, String uri, String query) {
        this.method = method;
        this.uri = uri;
        this.query = query;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getQuery() {
        return query;
    }
}
