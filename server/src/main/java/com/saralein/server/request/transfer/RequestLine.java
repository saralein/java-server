package com.saralein.server.request.transfer;

public class RequestLine {
    private final String method;
    private final String uri;

    public RequestLine(String method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }
}
