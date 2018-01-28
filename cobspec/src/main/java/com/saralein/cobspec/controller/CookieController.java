package com.saralein.cobspec.controller;

import com.saralein.cobspec.data.CookieStore;
import com.saralein.server.controller.Controller;
import com.saralein.server.cookies.Cookie;
import com.saralein.server.cookies.CookieParser;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

import java.util.List;

public class CookieController implements Controller {
    private final CookieParser parser;
    private final CookieStore store;

    public CookieController(CookieParser cookieParser, CookieStore cookieStore) {
        this.parser = cookieParser;
        this.store = cookieStore;
    }

    public Response respond(Request request) {
        if (hasCookieHeader(request)) {
            return getCookieResponse(request);
        }

        return getSetCookieResponse(request);
    }

    private Response getSetCookieResponse(Request request) {
        List<Cookie> cookies = parser.parseFromUri(request.getUri());
        store.addCookies(cookies);

        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/plain")
                .setCookies(cookies)
                .body("Eat")
                .build();
    }

    private Response getCookieResponse(Request request) {
        String cookiesUsed = getCookieHeader(request);
        List<Cookie> cookies = parser.parseFromHeader(cookiesUsed);

        return new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/plain")
                .body(createBody(cookies))
                .build();
    }

    private String createBody(List<Cookie> cookies) {
        StringBuilder builder = new StringBuilder();

        for (Cookie cookie : cookies) {
            if (store.containsCookie(cookie)) {
                String line = "mmmm " + cookie.getValue() + "\n";
                builder.append(line);
            }
        }

        return builder.toString();
    }

    private boolean hasCookieHeader(Request request) {
        return !getCookieHeader(request).isEmpty();
    }

    private String getCookieHeader(Request request) {
        return request.getHeader("Cookie");
    }
}
