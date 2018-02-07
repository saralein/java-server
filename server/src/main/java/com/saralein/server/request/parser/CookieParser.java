package com.saralein.server.request.parser;

import com.saralein.server.exchange.Cookie;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CookieParser {
    public List<Cookie> parse(String header) {
        return Arrays.stream(header.split(";\\s*"))
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
