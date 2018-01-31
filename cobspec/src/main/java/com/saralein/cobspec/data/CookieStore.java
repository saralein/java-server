package com.saralein.cobspec.data;

import com.saralein.server.cookies.Cookie;

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
        String lookup = cookie.toString();

        return cookies.stream()
                .map(Cookie::toString)
                .filter(storeCookie -> storeCookie.equals(lookup))
                .collect(Collectors.toList())
                .size() > 0;
    }
}
