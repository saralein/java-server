package com.saralein.cobspec.controller;

import com.saralein.cobspec.data.CookieStore;
import com.saralein.server.callable.Callable;
import com.saralein.server.exchange.Cookie;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CookieController implements Callable {
    private final CookieStore store;

    public CookieController(CookieStore cookieStore) {
        this.store = cookieStore;
    }

    public Response call(Request request) {
        if (hasParameters(request)) {
            return getSetCookieResponse(request);
        }

        return getCookieResponse(request);
    }

    private Response getSetCookieResponse(Request request) {
        List<Cookie> cookies = generateCookies(request.getParameters());
        store.addCookies(cookies);

        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/plain")
                .setCookies(cookies)
                .body("Eat")
                .build();
    }

    private Response getCookieResponse(Request request) {
        List<Cookie> cookies = request.getCookies();

        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/plain")
                .body(formatBody(cookies))
                .build();
    }

    private String formatBody(List<Cookie> cookies) {
        StringBuilder builder = new StringBuilder();

        for (Cookie cookie : cookies) {
            if (store.containsCookie(cookie)) {
                String line = "mmmm " + cookie.getValue() + "\n";
                builder.append(line);
            }
        }

        return builder.toString();
    }

    private boolean hasParameters(Request request) {
        return !request.getParameters().isEmpty();
    }

    private List<Cookie> generateCookies(Map<String, String> parameters) {
        List<Cookie> cookies = new ArrayList<>();

        for (String key : parameters.keySet()) {
            cookies.add(new Cookie(key, parameters.get(key)));
        }

        return cookies;
    }
}
