package com.saralein.server.request;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RequestTest {
    private Request request;

    @Before
    public void setUp() {
        request = new RequestParser().parse("GET /public/cheetara.jpg HTTP/1.1");
    }

    @Test
    public void returnsMethodOfRequest() {
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void returnsUriOfRequest() {
        assertEquals("/public/cheetara.jpg", request.getUri());
    }
}