package com.saralein.server.response;

import com.saralein.server.assertions.CookieAssertion;
import com.saralein.server.exchange.Cookie;
import com.saralein.server.exchange.Header;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ResponseTest {
    @Test
    public void addsStatusToResponse() {
        String status = "HTTP/1.1 200 OK\r\n\r\n";
        Response response = new Response.Builder()
                .status(200)
                .build();
        Header header = response.getHeader();

        assertEquals(status, header.formatToString());
    }

    @Test
    public void addsHeadersToResponse() {
        Map<String, String> expected = new HashMap<String, String>() {{
            put("Location", "/");
            put("Content-Type", "text/plain");
        }};
        Response response = new Response.Builder()
                .addHeader("Location", "/")
                .addHeader("Content-Type", "text/plain")
                .build();

        assertEquals(expected, response.getHeaders());
    }

    @Test
    public void addsCookiesToResponse() {
        List<Cookie> expected = new ArrayList<Cookie>() {{
            add(new Cookie("type", "chocolate"));
            add(new Cookie("amount", "12"));
        }};
        Response response = new Response.Builder()
                .setCookies(expected)
                .build();

        CookieAssertion.assertCookiesAreEqual(expected, response.getCookies());
    }

    @Test
    public void addsBodyToResponse() {
        byte[] body = "Hello Builder".getBytes();
        Response response = new Response.Builder()
                .body(body)
                .build();

        assertArrayEquals(body, response.getBody());
    }

}
