package com.saralein.server.request.transfer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RequestLineTest {
    private RequestLine requestLine;

    @Before
    public void setUp() {
        requestLine = new RequestLine("GET", "/stuff");
    }

    @Test
    public void retrievesMethod() {
        assertEquals("GET", requestLine.getMethod());
    }

    @Test
    public void retrievesUri() {
        assertEquals("/stuff", requestLine.getUri());
    }
}