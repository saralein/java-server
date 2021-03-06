package com.saralein.cobspec.data;

import com.saralein.server.exchange.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CookieStore {
    private List<Cookie> cookies;

    public CookieStore() {
        cookies = new ArrayList<>();
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void addCookies(List<Cookie> newCookies) {
        cookies.addAll(newCookies);
    }

    public boolean containsCookie(Cookie cookie) {
        return cookies.stream()
                .filter(storeCookie -> storeCookie.equals(cookie))
                .collect(Collectors.toList())
                .size() > 0;
    }
}
