package com.saralein.server.request;

import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RequestParserTest {
    private RequestParser requestParser;
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        requestParser = new RequestParser();
    }

    @Test
    public void correctlyParsesRequestForRoot() {
        Request request = new Request(new HashMap<String, String>() {{
            put("method", "GET");
            put("uri", "/");
            put("version", "HTTP/1.1");
        }});

        try {
            Request parsedRequest = requestParser.parse("GET / HTTP/1.1");
            assertEquals(request.getMethod(), parsedRequest.getMethod());
            assertEquals(request.getUri(), parsedRequest.getUri());
        } catch (Exception e) {
            fail("Failed to parse request for root.");
        }
    }

    @Test
    public void correctlyParsesRequestWithNoBody() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/cheetara.jpg");
            put("version", "HTTP/1.1");
        }});

        try {
            Request parsedRequest = requestParser.parse("GET /cheetara.jpg HTTP/1.1");
            assertEquals(request.getMethod(), parsedRequest.getMethod());
            assertEquals(request.getUri(), parsedRequest.getUri());
        } catch (Exception e) {
            fail("Failed to parse request with no body.");
        }
    }

    @Test
    public void returnsRequestWithBody() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "POST");
            put("uri", "/form");
            put("version", "HTTP/1.1");
            put("body", "My=Data&More=Data");
        }});

        try {
            Request parsedRequest = requestParser.parse("POST /form HTTP/1.1\r\n\r\nbody: My=Data&More=Data");
            assertEquals(request.getMethod(), parsedRequest.getMethod());
            assertEquals(request.getUri(), parsedRequest.getUri());
            assertEquals(request.getBody(), parsedRequest.getBody());
        } catch (Exception e) {
            fail("Failed to parse request with body.");
        }
    }

    @Test
    public void throwsErrorForBadRequest() {
        try {
            requestParser.parse("");
            fail("Failed to throw error for bad request.");
        } catch (Exception ignored) { }
    }
}