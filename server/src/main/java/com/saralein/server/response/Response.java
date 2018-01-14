package com.saralein.server.response;

import com.saralein.server.protocol.StatusCodes;

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

        public Builder addBody(String body) {
            this.body = body.getBytes();
            return this;
        }

        public Builder addBody(byte[] body) {
            this.body = body;
            return this;
        }

        public Builder addBodyByStatus(int code) {
            this.body = StatusCodes.retrieve(code).getBytes();
            return this;
        }

        public Builder addStatus(int code) {
            header.addStatus(code);
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
