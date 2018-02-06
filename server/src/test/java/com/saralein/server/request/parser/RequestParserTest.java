package com.saralein.server.request.parser;

import com.saralein.server.request.Request;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class RequestParserTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    private RequestParser requestParser;

    @Before
    public void setUp() {
        requestParser = new RequestParser(
                new RequestLineParser(), new HeaderParser(), new ParameterParser());
    }

    @Test
    public void parsesRequestForRoot() throws Exception {
        Request parsedRequest = requestParser.parse("GET / HTTP/1.1\r\nContent-Type: text/html\r\n\r\n");

        assertEquals("GET", parsedRequest.getMethod());
        assertEquals("/", parsedRequest.getUri());
        assertEquals("text/html", parsedRequest.getHeader("Content-Type"));
    }

    @Test
    public void parsesRequestWithNoBody() throws Exception {
        Request parsedRequest = requestParser.parse("GET /cheetara.jpg HTTP/1.1\r\nContent-Type: text/html\r\n\r\n");

        assertEquals("GET", parsedRequest.getMethod());
        assertEquals("/cheetara.jpg", parsedRequest.getUri());
        assertEquals("text/html", parsedRequest.getHeader("Content-Type"));
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
    public void parsesRequestWithQueryInRequestLine() throws Exception {
        String rawRequest = "GET /birds?type=chicken&number=6 HTTP/1.1\r\n\r\n";
        Request parsedRequest = requestParser.parse(rawRequest);
        Map<String, String> parameters = new HashMap<String, String>() {{
            put("type", "chicken");
            put("number", "6");
        }};

        assertEquals("GET", parsedRequest.getMethod());
        assertEquals("/birds", parsedRequest.getUri());
        assertEquals(parameters, parsedRequest.getParameters());
    }

    @Test(expected = Exception.class)
    public void throwsErrorForBadRequest() throws Exception {
        requestParser.parse("");
    }
}
