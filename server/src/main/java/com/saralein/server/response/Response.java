package com.saralein.server.response;

import com.saralein.server.exchange.Cookie;
import com.saralein.server.exchange.Header;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Response {
    private final Header header;
    private final List<Cookie> cookies;
    private final byte[] body;

    public Response(Header header, List<Cookie> cookies, byte[] body) {
        this.header = header;
        this.cookies = cookies;
        this.body = body;
    }

    public Header getHeader() {
        return header;
    }

    public Map<String, String> getHeaders() {
        return header.getHeaders();
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public byte[] getBody() {
        return body;
    }

    public static class Builder {
        private byte[] body = new byte[]{};
        private Header header = new Header();
        private List<Cookie> cookies = new ArrayList<>();

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

        public Builder setCookies(List<Cookie> cookies) {
            this.cookies.addAll(cookies);
            return this;
        }

        public Response build() {
            return new Response(header, cookies, body);
        }
    }
}
