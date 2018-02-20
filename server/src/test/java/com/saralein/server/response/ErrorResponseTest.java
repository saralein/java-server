package com.saralein.server.response;

import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ErrorResponseTest {
    private Map<String, String> headers;

    @Before
    public void setUp() {
        headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
    }

    @Test
    public void returnsResponseWithStatus() {
        Response response = new ErrorResponse(404).respond();
        
        assertEquals(404, response.getStatus());
        assertEquals(headers, response.getHeaders());
        assertArrayEquals("404 Not Found".getBytes(), response.getBody());
    }

    @Test
    public void returnsResponseWithStatusAndProvidedHeader() {
        headers.put("Content-Range", "*/10");
        Response response = new ErrorResponse(416).respond("Content-Range", "*/10");

        assertEquals(416, response.getStatus());
        assertEquals(headers, response.getHeaders());
        assertArrayEquals("416 Range Not Satisfiable".getBytes(), response.getBody());
    }
}
