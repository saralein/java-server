package com.saralein.server.request.parser;

import com.saralein.server.request.transfer.RequestLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RequestLineParserTest {
    private RequestLineParser requestLineParser;

    @Before
    public void setUp() {
        requestLineParser = new RequestLineParser();
    }

    @Test
    public void parsesSimpleRequestLine() throws Exception {
        List<String> request = new ArrayList<String>() {{
            add("GET /birds HTTP/1.1");
        }};

        RequestLine requestLine = requestLineParser.parse(request);

        assertEquals("GET", requestLine.getMethod());
        assertEquals("/birds", requestLine.getUri());
    }
}
