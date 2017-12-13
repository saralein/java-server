package com.saralein.cobspec.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class NotFoundControllerTest {
    private Response notFoundResponse;

    @Before
    public void setUp() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/snarf.jpg");
            put("version", "HTTP/1.1");
        }});
        NotFoundController notFoundController = new NotFoundController();
        notFoundResponse = notFoundController.createResponse(request);
    }

    @Test
    public void returnsResponseWithCorrectHeader() {
        Header header = notFoundResponse.getHeader();

        assertEquals("HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
    }

    @Test
    public void returnsResponseWithCorrectBody() {
        assertArrayEquals("<center><h1>404</h1>Page not found.</center>".getBytes(), notFoundResponse.getBody());
    }
}