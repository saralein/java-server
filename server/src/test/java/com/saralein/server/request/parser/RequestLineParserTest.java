package com.saralein.server.request.parser;

import com.saralein.server.exchange.RequestLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RequestLineParserTest {
    private RequestLineParser requestLineParser;

    @Before
    public void setUp() {
        requestLineParser = new RequestLineParser();
    }

    @Test
    public void parsesSimpleRequestLine() throws Exception {
        RequestLine requestLine = requestLineParser.parse("GET /birds HTTP/1.1");

        assertEquals("GET", requestLine.getMethod());
        assertEquals("/birds", requestLine.getUri());
        assertEquals("", requestLine.getQuery());
    }

    @Test
    public void parsesRequestLineWithQuery() throws Exception {
        RequestLine requestLine = requestLineParser.parse("GET /birds?type=chicken HTTP/1.1");

        assertEquals("GET", requestLine.getMethod());
        assertEquals("/birds", requestLine.getUri());
        assertEquals("type=chicken", requestLine.getQuery());
    }
}
