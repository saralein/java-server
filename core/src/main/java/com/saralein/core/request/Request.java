package com.saralein.core.request;

import java.util.HashMap;

public class Request {
    private final HashMap<String, String> request;

    public Request(HashMap<String, String> request) {
        this.request = request;
    }

    public String getMethod() {
        return request.get("method");
    }

    public String getUri() {
        return request.get("uri");
    }

    public String getBody() {
        return request.getOrDefault("body", "");
    }
}
