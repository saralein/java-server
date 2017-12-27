package com.saralein.server.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ErrorControllerTest {
    private Request request;
    private ErrorController errorController;

    @Before
    public void setUp() {
        request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/snarf.jpg");
            put("version", "HTTP/1.1");
        }});

        errorController = new ErrorController();
    }

    @Test
    public void returnsCorrectResponseForNotFound() {
        Response notFoundResponse = errorController.createResponse(request);

        Header header = notFoundResponse.getHeader();

        assertEquals("HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("404 Not Found".getBytes(), notFoundResponse.getBody());
    }

    @Test
    public void returnsCorrectResponseForNotAllowed() {
        ErrorController notAllowedController =  errorController.updateStatus(405);
        Response notAllowedResponse = notAllowedController.createResponse(request);

        Header header = notAllowedResponse.getHeader();

        assertEquals("HTTP/1.1 405 Method Not Allowed\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("405 Method Not Allowed".getBytes(), notAllowedResponse.getBody());
    }
}