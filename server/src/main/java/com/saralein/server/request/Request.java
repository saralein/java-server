package com.saralein.server.request;

import java.util.HashMap;

public class Request {
    private final HashMap<String, String> request;
    private String method;
    private String uri;
    private String body;

    public Request(String method, String uri, String body, HashMap<String, String> headers) {
        this.method = method;
        this.uri = uri;
        this.body = body;
        this.request = headers;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getBody() {
        return body;
    }

    public String getHeader(String key) {
        return request.getOrDefault(key, "");
    }

    public String getRequestLine() {
        return String.format("%s %s HTTP/1.1", getMethod(), getUri());
    }

    public static class Builder {
        private String method = "";
        private String uri = "";
        private String body = "";
        private HashMap<String, String> headers = new HashMap<>();

        public Builder addMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder addUri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder addBody(String body) {
            this.body = body;
            return this;
        }

        public Builder addHeader(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public Builder addHeaders(HashMap<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Request build() {
            return new Request(method, uri, body, headers);
        }
    }
}
