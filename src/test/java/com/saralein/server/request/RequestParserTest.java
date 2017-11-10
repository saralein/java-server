package com.saralein.server.request;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class RequestParserTest {
    private String request1 = "GET / HTTP/1.1";
    private String request2 = "GET /public/cheetara.jpg HTTP/1.1";
    private HashMap<String, String> testRequest1 = null;
    private HashMap<String, String> testRequest2 = null;

    @Before
    public void setUp() {
        testRequest1 = new HashMap<String, String>() {{
            put("method", "GET");
            put("uri", "/");
            put("version", "HTTP/1.1");
        }};

        testRequest2 = new HashMap<String, String>() {{
            put("method", "GET");
            put("uri", "/public/cheetara.jpg");
            put("version", "HTTP/1.1");
        }};
    }

    @Test
    public void parsesRequestLineFromClientForBaseUri() {
        RequestParser requestParser = new RequestParser();
        HashMap<String, String> parsedRequest = requestParser.parse(request1);

        assertTrue(parsedRequest.containsKey("method"));
        assertTrue(parsedRequest.containsKey("uri"));
        assertTrue(parsedRequest.containsKey("version"));

        assertEquals(testRequest1.get("method"), parsedRequest.get("method"));
        assertEquals(testRequest1.get("uri"), parsedRequest.get("uri"));
        assertEquals(testRequest1.get("version"), parsedRequest.get("version"));
    }

    @Test
    public void parsesRequestLineFromClientForMoreComplexUri() {
        RequestParser requestParser = new RequestParser();
        HashMap<String, String> parsedRequest = requestParser.parse(request2);

        assertTrue(parsedRequest.containsKey("method"));
        assertTrue(parsedRequest.containsKey("uri"));
        assertTrue(parsedRequest.containsKey("version"));

        assertEquals(testRequest2.get("method"), parsedRequest.get("method"));
        assertEquals(testRequest2.get("uri"), parsedRequest.get("uri"));
        assertEquals(testRequest2.get("version"), parsedRequest.get("version"));
    }
}