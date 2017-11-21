package com.saralein.server.response;

public class ResponseBuilder {
    byte[] body;
    Header header;

    public ResponseBuilder() {
        this.header = new Header();
    }

    public ResponseBuilder addBody(String body) {
        this.body = body.getBytes();
        return this;
    }

    public ResponseBuilder addBody(byte[] body) {
        this.body = body;
        return this;
    }

    public ResponseBuilder addStatus(int code) {
        header.addStatus(code);
        return this;
    }

    public ResponseBuilder addHeader(String title, String content) {
        header.addHeader(title, content);
        return this;
    }

    public Response build() {
        return new Response(header, body);
    }
}
