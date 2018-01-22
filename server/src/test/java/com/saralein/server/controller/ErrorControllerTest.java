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
        request = new Request.Builder()
                .method("GET")
                .uri("/snarf.jpg")
                .build();

        errorController = new ErrorController();
    }

    @Test
    public void returnsCorrectResponseForNotFound() {
        Response notFoundResponse = errorController.respond(request);

        Header header = notFoundResponse.getHeader();

        assertEquals("HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("404 Not Found".getBytes(), notFoundResponse.getBody());
    }

    @Test
    public void returnsCorrectResponseForNotAllowed() {
        ErrorController notAllowedController =  errorController.updateStatus(405);
        Response notAllowedResponse = notAllowedController.respond(request);

        Header header = notAllowedResponse.getHeader();

        assertEquals("HTTP/1.1 405 Method Not Allowed\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("405 Method Not Allowed".getBytes(), notAllowedResponse.getBody());
    }
}