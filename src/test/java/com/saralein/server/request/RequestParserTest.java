package com.saralein.server.request;

import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RequestParserTest {
    private Request test1Request;
    private Request test2Request;
    private RequestParser requestParser;

    @Before
    public void setUp() {
        HashMap<String, String> test1Hash = new HashMap<String, String>() {{
            put("method", "GET");
            put("uri", "/");
            put("version", "HTTP/1.1");
        }};

        HashMap<String, String> test2Hash = new HashMap<String, String>() {{
            put("method", "GET");
            put("uri", "/public/cheetara.jpg");
            put("version", "HTTP/1.1");
        }};

        test1Request = new Request(test1Hash);
        test2Request = new Request(test2Hash);

        requestParser = new RequestParser();
    }

    @Test
    public void returnsRequestWithCorrectInformation() {
        Request test1ThroughParser = requestParser.parse("GET / HTTP/1.1");

        assertEquals(test1Request.getMethod(), test1ThroughParser.getMethod());
        assertEquals(test1Request.getUri(), test1ThroughParser.getUri());

        Request test2ThroughParser = requestParser.parse("GET /public/cheetara.jpg HTTP/1.1");

        assertEquals(test2Request.getMethod(), test2ThroughParser.getMethod());
        assertEquals(test2Request.getUri(), test2ThroughParser.getUri());
    }
}