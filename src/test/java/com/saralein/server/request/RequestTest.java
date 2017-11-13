package com.saralein.server.request;

import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RequestTest {
    private String requestString = "GET /public/cheetara.jpg HTTP/1.1";
    private HashMap<String, String> parsedRequest;
    private Request request;

    @Before
    public void setUp() {
        request = new RequestParser().parse(requestString);
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