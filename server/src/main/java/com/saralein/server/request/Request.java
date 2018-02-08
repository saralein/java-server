package com.saralein.server.request;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private final Map<String, String> request;
    private final Map<String, String> parameters;
    private final String method;
    private final String uri;
    private final String body;

    public Request(String method, String uri, Map<String, String> parameters, String body, Map<String, String> headers) {
        this.method = method;
        this.uri = uri;
        this.parameters = parameters;
        this.body = body;
        this.request = headers;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public Map<String, String> getParameters() {
        return parameters;
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
        private Map<String, String> parameters = new HashMap<>();
        private Map<String, String> headers = new HashMap<>();

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder parameters(Map<String, String> parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder addHeader(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public Builder addHeaders(Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Request build() {
            return new Request(method, uri, parameters, body, headers);
        }
    }
}
