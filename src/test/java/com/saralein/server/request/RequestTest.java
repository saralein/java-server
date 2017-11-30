package com.saralein.server.request;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class RequestTest {
    private Request request;
    private Request requestWithoutBody;

    @Before
    public void setUp() {
        request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/cheetara.jpg");
            put("version", "HTTP/1.1");
            put("body", "Hello");
        }});

        requestWithoutBody = new Request(new HashMap<String, String>(){{
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
        assertEquals("", requestWithoutBody.getBody());
    }
}