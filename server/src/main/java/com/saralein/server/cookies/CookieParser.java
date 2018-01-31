package com.saralein.server.cookies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CookieParser {
    public List<Cookie> parseFromUri(String uri) {
        String[] splitUriAndCookies = uri.split("\\?");

        if (splitUriAndCookies.length > 1) {
            String cookies = splitUriAndCookies[1];
            return mapCookies(cookies, "&");
        }

        return new ArrayList<>();
    }

    public List<Cookie> parseFromHeader(String header) {
        return mapCookies(header, ";\\s*");
    }

    private List<Cookie> mapCookies(String cookies, String delimiter) {
        return Arrays.stream(cookies.split(delimiter))
                .filter(this::isNotEmpty)
                .filter(this::matchesCookiePattern)
                .map(this::generateCookie)
                .collect(Collectors.toList());
    }

    private boolean isNotEmpty(String cookie) {
        return !cookie.isEmpty();
    }

    private boolean matchesCookiePattern(String cookie) {
        return cookie.matches("^(?:(\\w|\\pP)+=(\\w|\\pP)+)$");
    }

    private Cookie generateCookie(String cookie) {
        String[] parsedCookie = cookie.split("=");
        return new Cookie(parsedCookie[0], parsedCookie[1]);
    }
}
