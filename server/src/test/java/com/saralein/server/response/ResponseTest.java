package com.saralein.server.response;

import com.saralein.server.assertions.CookieAssertion;
import com.saralein.server.exchange.Cookie;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ResponseTest {
    private Map<String, String> headers;
    private List<Cookie> cookies;

    @Before
    public void setUp() {
        headers = new HashMap<String, String>() {{
            put("Location", "/");
            put("Content-Type", "text/plain");
        }};
        cookies = new ArrayList<Cookie>() {{
            add(new Cookie("type", "chocolate"));
            add(new Cookie("amount", "12"));
        }};
    }

    @Test
    public void addsStatusToResponse() {
        Response response = new Response.Builder()
                .status(200)
                .build();

        assertEquals(200, response.getStatus());
    }

    @Test
    public void addsHeadersToResponse() {
        Response response = new Response.Builder()
                .addHeader("Location", "/")
                .addHeader("Content-Type", "text/plain")
                .build();

        assertEquals(headers, response.getHeaders());
    }

    @Test
    public void addsCookiesToResponse() {
        Response response = new Response.Builder()
                .setCookies(cookies)
                .build();

        CookieAssertion.assertCookiesAreEqual(cookies, response.getCookies());
    }

    @Test
    public void addsBodyToResponse() {
        byte[] body = "Hello Builder".getBytes();
        Response response = new Response.Builder()
                .body(body)
                .build();

        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void comparesResponseEquality() {
        Response response = new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/plain")
                .addHeader("Content-Length", "12")
                .setCookies(cookies)
                .body("Hello")
                .build();

        Response matching = new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/plain")
                .addHeader("Content-Length", "12")
                .setCookies(cookies)
                .body("Hello")
                .build();

        assert (response.equals(matching) && matching.equals(response));
        assert (response.hashCode() == matching.hashCode());
    }
}
