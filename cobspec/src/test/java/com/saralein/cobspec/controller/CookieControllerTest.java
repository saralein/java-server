package com.saralein.cobspec.controller;

import com.saralein.cobspec.data.CookieStore;
import com.saralein.server.cookies.Cookie;
import com.saralein.server.cookies.CookieParser;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CookieControllerTest {
    private final Cookie CHOCOLATE_COOKIE = new Cookie("type", "chocolate");
    private final Cookie BAKER_COOKIE = new Cookie("baker", "Phil");
    private final List<Cookie> COOKIES = new ArrayList<Cookie>() {{
        add(CHOCOLATE_COOKIE);
        add(BAKER_COOKIE);
    }};
    private CookieController cookieController;
    private CookieStore cookieStore;

    @Before
    public void setUp() {
        cookieStore = new CookieStore();
        cookieController = new CookieController(
                new CookieParser(), cookieStore);
    }

    @Test
    public void respondsWithSetCookieAndAddsCookiesToStore() {
        Request request = new Request.Builder()
                .uri("/cookie?type=chocolate&baker=Phil")
                .method("GET")
                .build();
        Response response = cookieController.respond(request);
        Header header = response.getHeader();
        String expectedHeader = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n" +
                "Set-Cookie: type=chocolate\r\nSet-Cookie: baker=Phil\r\n\r\n";

        assertEquals(expectedHeader, header.formatToString());
        assertTrue(cookieStore.containsCookie(CHOCOLATE_COOKIE));
        assertTrue(cookieStore.containsCookie(new Cookie("baker", "Phil")));
    }

    @Test
    public void usesCookiesToRespond() {
        cookieStore.addCookies(COOKIES);
        Request request = new Request.Builder()
                .uri("/eat_cookie")
                .addHeader("Cookie", "baker=Phil; type=chocolate")
                .build();
        Response response = cookieController.respond(request);

        String expected = "mmmm Phil\nmmmm chocolate\n";

        assertArrayEquals(expected.getBytes(), response.getBody());
    }

    @Test
    public void excludesCookieNotInStoreInResponse() {
        cookieStore.addCookies(COOKIES);
        Request request = new Request.Builder()
                .uri("/eat_cookie")
                .addHeader("Cookie", "baker=Phil; type=chocolate; amount=12")
                .build();
        Response response = cookieController.respond(request);

        String expected = "mmmm Phil\nmmmm chocolate\n";

        assertArrayEquals(expected.getBytes(), response.getBody());
    }
}
