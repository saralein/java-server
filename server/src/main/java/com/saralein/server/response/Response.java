package com.saralein.server.response;

public class Response {
    private final Header header;
    private final byte[] body;

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

    public static class Builder {
        private byte[] body = new byte[]{};
        private Header header = new Header();

        public Builder body(String body) {
            this.body = body.getBytes();
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public Builder status(int code) {
            header.status(code);
            return this;
        }

        public Builder addHeader(String title, String content) {
            header.addHeader(title, content);
            return this;
        }

        public Response build() {
            return new Response(header, body);
        }
    }
}
