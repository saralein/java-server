package com.saralein.server.response;

import com.saralein.server.exchange.Cookie;
import com.saralein.server.exchange.Headers;
import java.util.*;

public class Response {
    private final int status;
    private final Headers headers;
    private final List<Cookie> cookies;
    private final byte[] body;

    public Response(int status, Headers headers, List<Cookie> cookies, byte[] body) {
        this.status = status;
        this.headers = headers;
        this.cookies = cookies;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return headers.getHeaders();
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Response response = (Response) o;

        if (status != response.status) return false;
        if (!headers.equals(response.headers)) return false;
        if (!new HashSet<>(cookies).equals(new HashSet<>(response.cookies))) return false;
        return Arrays.equals(body, response.body);
    }

    @Override
    public int hashCode() {
        int result = status;
        result = 31 * result + headers.hashCode();
        result = 31 * result + cookies.hashCode();
        result = 31 * result + Arrays.hashCode(body);
        return result;
    }

    public static class Builder {
        private byte[] body = new byte[]{};
        private Headers headers = new Headers();
        private List<Cookie> cookies = new ArrayList<>();
        private int status;

        public Builder body(String body) {
            this.body = body.getBytes();
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder addHeader(String title, String content) {
            headers.addHeader(title, content);
            return this;
        }

        public Builder setCookies(List<Cookie> cookies) {
            this.cookies.addAll(cookies);
            return this;
        }

        public Response build() {
            return new Response(status, headers, cookies, body);
        }
    }
}
