package com.saralein.server.response;

import com.saralein.server.exchange.Header;
import com.saralein.server.exchange.Message;

public class Response {
    private final Header header;
    private final byte[] body;
    private final Message message;

    public Response(Header header, byte[] body, Message message) {
        this.header = header;
        this.body = body;
        this.message = message;
    }

    public Header getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }

    public String summary() {
        return message.summary(header);
    }

    public byte[] full() {
        return message.full(header, body);
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
            return new Response(header, body, new Message());
        }
    }
}
