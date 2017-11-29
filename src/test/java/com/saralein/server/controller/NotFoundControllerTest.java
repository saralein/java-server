package com.saralein.server.controller;

import com.saralein.server.Controller.NotFoundController;
import com.saralein.server.request.Request;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NotFoundControllerTest {
    private Response notFoundResponse;

    @Before
    public void setUp() {
        RequestParser requestParser = new RequestParser();
        Request request = requestParser.parse("GET /snarf.jpg HTTP/1.1");
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