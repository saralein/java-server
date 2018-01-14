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
    public void correctlyParsesRequestForRoot() throws Exception {
        Request request = new Request.Builder()
                .addMethod("GET")
                .addUri("/")
                .build();

        Request parsedRequest = requestParser.parse("GET / HTTP/1.1");
        assertEquals(request.getMethod(), parsedRequest.getMethod());
        assertEquals(request.getUri(), parsedRequest.getUri());
    }

    @Test
    public void correctlyParsesRequestWithNoBody() throws Exception {
        Request request = new Request.Builder()
                .addMethod("GET")
                .addUri("/cheetara.jpg")
                .build();

        Request parsedRequest = requestParser.parse("GET /cheetara.jpg HTTP/1.1");
        assertEquals(request.getMethod(), parsedRequest.getMethod());
        assertEquals(request.getUri(), parsedRequest.getUri());
    }

    @Test
    public void returnsRequestWithBody() throws Exception {
        Request request = new Request.Builder()
                .addMethod("POST")
                .addUri("/form")
                .addBody("My=Data&More=Data")
                .build();

        Request parsedRequest = requestParser.parse("POST /form HTTP/1.1\r\n\r\nbody: My=Data&More=Data");
        assertEquals(request.getMethod(), parsedRequest.getMethod());
        assertEquals(request.getUri(), parsedRequest.getUri());
        assertEquals(request.getBody(), parsedRequest.getBody());
    }

    @Test(expected = Exception.class)
    public void throwsErrorForBadRequest() throws Exception {
        requestParser.parse("");
    }
}