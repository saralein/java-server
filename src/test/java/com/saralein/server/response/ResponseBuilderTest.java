package com.saralein.server.response;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ResponseBuilderTest {
    private byte[] responseArray;

    @Before
    public void setUp() {
        String response = "HTTP/1.1 200 OK\r\n" +
                          "Content-Type: text/html\r\n\r\n" +
                          "<html><head></head><body><h1>Response from Server</h1></body></html>";
        responseArray = response.getBytes();
    }

    @Test
    public void createsResponse() {
        assertArrayEquals(responseArray, ResponseBuilder.createResponse());
    }
}