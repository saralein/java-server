package com.saralein.cobspec.controller;

import com.saralein.cobspec.data.CookieStore;
import com.saralein.server.exchange.Cookie;
import com.saralein.server.exchange.Header;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class CookieControllerTest {
    private Cookie chocolateCookie;
    private Cookie bakerCookie;
    private List<Cookie> cookies;
    private CookieController cookieController;
    private CookieStore cookieStore;

    private boolean cookiesAreEqual(List<Cookie> expected, List<Cookie> actual) {
        expected.sort(Cookie::compareTo);
        actual.sort(Cookie::compareTo);
        return expected.equals(actual);
    }

    @Before
    public void setUp() {
        chocolateCookie = new Cookie("type", "chocolate");
        bakerCookie = new Cookie("baker", "Phil");
        cookies = new ArrayList<Cookie>() {{
            add(chocolateCookie);
            add(bakerCookie);
        }};
        cookieStore = new CookieStore();
        cookieController = new CookieController(cookieStore);
    }

    @Test
    public void respondsWithSetCookieAndAddsCookiesToStore() {
        Map<String, String> parameters = new HashMap<String, String>() {{
            put("baker", "Phil");
            put("type", "chocolate");
        }};
        Request request = new Request.Builder()
                .uri("/cookie")
                .method("GET")
                .parameters(parameters)
                .build();
        Response response = cookieController.respond(request);
        Header header = response.getHeader();
        String expectedHeader = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n";

        assertEquals(expectedHeader, header.formatToString());
        assert (cookiesAreEqual(cookies, response.getCookies()));
        assertTrue(cookieStore.containsCookie(chocolateCookie));
        assertTrue(cookieStore.containsCookie(new Cookie("baker", "Phil")));
    }

    @Test
    public void usesCookiesToRespond() {
        cookieStore.addCookies(cookies);
        Request request = new Request.Builder()
                .uri("/eat_cookie")
                .cookies(cookies)
                .build();
        Response response = cookieController.respond(request);
        String expected = "mmmm chocolate\nmmmm Phil\n";

        assertArrayEquals(expected.getBytes(), response.getBody());
    }

    @Test
    public void excludesCookieNotInStoreInResponse() {
        cookieStore.addCookies(cookies);
        cookies.add(new Cookie("amount", "12"));
        Request request = new Request.Builder()
                .uri("/eat_cookie")
                .cookies(cookies)
                .build();
        Response response = cookieController.respond(request);
        String expected = "mmmm chocolate\nmmmm Phil\n";

        assertArrayEquals(expected.getBytes(), response.getBody());
    }
}
