package com.saralein.server.request;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

public class RequestTest {
    private Request request;
    private Request requestLineOnly;

    @Before
    public void setUp() {
        request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/cheetara.jpg");
            put("version", "HTTP/1.1");
            put("body", "Hello");
            put("Authorization", "encodedCredentials");
        }});

        requestLineOnly= new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/cheetara.jpg");
            put("version", "HTTP/1.1");
        }});
    }

    @Test
    public void returnsMethodOfRequest() {
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void returnsUriOfRequest() {
        assertEquals("/cheetara.jpg", request.getUri());
    }

    @Test
    public void returnsBodyOfRequest() {
        assertEquals("Hello", request.getBody());
        assertEquals("", requestLineOnly.getBody());
    }

    @Test
    public void returnsAuthCredentials() {
        assertEquals("encodedCredentials", request.getAuthorization());
        assertEquals("", requestLineOnly.getAuthorization());
    }

    @Test
    public void returnsFullRequestLine() {
        assertEquals("GET /cheetara.jpg HTTP/1.1", request.getRequestLine());
    }
}