package com.saralein.server.response;

import com.saralein.server.exchange.Cookie;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ResponseSerializerTest {
    private ResponseSerializer responseSerializer;

    @Before
    public void setUp() {
        responseSerializer = new ResponseSerializer();
    }

    @Test
    public void convertsResponseHeaderAndBodyToByteArray() {
        Response response = new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/html")
                .body("<h1>I AM A HEADER</h1>")
                .build();
        String responseString = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n\r\n" +
                "<h1>I AM A HEADER</h1>";
        byte[] responseBytes = responseString.getBytes();
        assertEquals(new String(responseBytes), new String(responseSerializer.convertToBytes(response)));
    }

    @Test
    public void convertsResponseHeaderCookiesAndBodyToByteArray() {
        List<Cookie> cookies = new ArrayList<Cookie>() {{
            add(new Cookie("type", "chocolate"));
            add(new Cookie("baker", "Phil"));
        }};
        Response response = new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/html")
                .setCookies(cookies)
                .body("<h1>I AM A HEADER</h1>")
                .build();
        String responseString = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n" +
                "Set-Cookie: type=chocolate\r\nSet-Cookie: baker=Phil\r\n\r\n" +
                "<h1>I AM A HEADER</h1>";
        byte[] responseBytes = responseString.getBytes();
        assertArrayEquals(responseBytes, responseSerializer.convertToBytes(response));
    }
}
