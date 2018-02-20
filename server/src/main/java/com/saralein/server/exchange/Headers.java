package com.saralein.server.exchange;

import java.util.HashMap;
import java.util.Map;

public class Headers {
    private HashMap<String, String> headers;

    public Headers() {
        this.headers = new HashMap<>();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String title, String content) {
        headers.put(title, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Headers headers1 = (Headers) o;

        return headers.equals(headers1.headers);
    }

    @Override
    public int hashCode() {
        return headers.hashCode();
    }
}
