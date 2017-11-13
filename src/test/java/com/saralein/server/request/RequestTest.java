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
        parsedRequest = new RequestParser().parse(requestString);
        request = new Request(parsedRequest);
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