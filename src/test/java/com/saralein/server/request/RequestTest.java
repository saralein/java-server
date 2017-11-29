package com.saralein.server.request;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RequestTest {
    private Request request;
    private Request requestWithoutBody;

    @Before
    public void setUp() {
        request = new RequestParser().parse("GET /public/cheetara.jpg HTTP/1.1\r\n\r\nBody: Hello");
        requestWithoutBody = new RequestParser().parse("GET /public/cheetara.jpg HTTP/1.1\r\n");
    }

    @Test
    public void returnsMethodOfRequest() {
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void returnsUriOfRequest() {
        assertEquals("/public/cheetara.jpg", request.getUri());
    }

    @Test
    public void returnsBodyOfRequest() {
        assertEquals("Hello", request.getBody());
        assertEquals("", requestWithoutBody.getBody());
    }
}