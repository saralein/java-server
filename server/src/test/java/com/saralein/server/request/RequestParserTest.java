package com.saralein.server.request;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

public class RequestParserTest {
    private RequestParser requestParser;
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        requestParser = new RequestParser();
    }

    @Test
    public void parsesRequestForRoot() throws Exception {
        Request parsedRequest = requestParser.parse("GET / HTTP/1.1\r\nContent-Type: text/html");

        assertEquals("GET", parsedRequest.getMethod());
        assertEquals("/", parsedRequest.getUri());
        assertEquals("text/html", parsedRequest.getHeader("Content-Type"));
    }

    @Test
    public void parsesRequestWithNoBody() throws Exception {
        Request parsedRequest = requestParser.parse("GET /cheetara.jpg HTTP/1.1\r\nContent-Type: text/html");

        assertEquals("GET", parsedRequest.getMethod());
        assertEquals("/cheetara.jpg", parsedRequest.getUri());
        assertEquals("text/html", parsedRequest.getHeader("Content-Type"));
    }

    @Test
    public void parsesRequestWithZeroContentLength() throws Exception {
        Request parsedRequest = requestParser.parse("GET / HTTP/1.1\r\nContent-Length: 0");

        assertEquals("GET", parsedRequest.getMethod());
        assertEquals("/", parsedRequest.getUri());
        assertEquals("0", parsedRequest.getHeader("Content-Length"));
        assertEquals("", parsedRequest.getBody());
    }

    @Test
    public void parsesRequestWithBody() throws Exception {
        String rawRequest = "POST /form HTTP/1.1\r\nContent-Length: 17\r\n\r\nMy=Data&More=Data";
        Request parsedRequest = requestParser.parse(rawRequest);

        assertEquals("POST", parsedRequest.getMethod());
        assertEquals("/form", parsedRequest.getUri());
        assertEquals("My=Data&More=Data", parsedRequest.getBody());
    }

    @Test
    public void parsesRequestWhenBodyStartsWithContentLength() throws Exception {
        String rawRequest = "GET / HTTP/1.1\r\nContent-Length: 26\r\n\r\nContent-Length is a header";
        Request parsedRequest = requestParser.parse(rawRequest);

        assertEquals("GET", parsedRequest.getMethod());
        assertEquals("/", parsedRequest.getUri());
        assertEquals("26", parsedRequest.getHeader("Content-Length"));
        assertEquals("Content-Length is a header", parsedRequest.getBody());
    }

    @Test(expected = Exception.class)
    public void throwsErrorForBadRequest() throws Exception {
        requestParser.parse("");
    }
}