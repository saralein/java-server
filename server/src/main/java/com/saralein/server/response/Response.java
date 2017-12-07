package com.saralein.server.response;

public class Response {
    private Header header;
    private byte[] body;

    public Response(Header header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public Header getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }
}
