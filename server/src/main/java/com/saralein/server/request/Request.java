package com.saralein.server.request;

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
  
    public String getAuthorization() {
        return request.getOrDefault("Authorization", "");
    }

    public String getRequestLine() {
        return String.format("%s %s HTTP/1.1", getMethod(), getUri());
    }
}
