package com.saralein.server.response;

public class Response {
    private Header header;
    private byte[] body;

    public Response(Header header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public byte[] convertToBytes() {
        byte[] headerBytes = header.convertToBytes();

        if (body != null) {
            return combineResponseParts(headerBytes, body);
        } else {
            return headerBytes;
        }
    }

    private byte[] combineResponseParts(byte[] headers, byte[] body) {
        byte[] combined = new byte[headers.length + body.length];

        for (int i = 0; i < combined.length; ++i) {
            combined[i] = i < headers.length ? headers[i] : body[i - headers.length];
        }

        return combined;
    }
}
