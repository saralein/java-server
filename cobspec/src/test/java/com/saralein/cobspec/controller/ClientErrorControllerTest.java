package com.saralein.cobspec.controller;

import com.saralein.server.controller.ErrorController;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClientErrorControllerTest {
    private Request request;
    private ClientErrorController errorController;

    @Before
    public void setUp() {
        request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/snarf.jpg");
            put("version", "HTTP/1.1");
        }});
        HashMap<Integer, String> errorMessages = new HashMap<Integer, String>(){{
            put(404, "<center><h1>404</h1>Page not found.</center>");
            put(405, "<center><h1>405</h1>Method not allowed.</center>");
        }};

        errorController = new ClientErrorController(errorMessages);

    }

    @Test
    public void returnsCorrectResponseForNotFound() {
        Response notFoundResponse = errorController.createResponse(request);

        Header header = notFoundResponse.getHeader();

        assertEquals("HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("<center><h1>404</h1>Page not found.</center>".getBytes(), notFoundResponse.getBody());
    }

    @Test
    public void returnsCorrectResponseForNotAllowed() {
        ErrorController notAllowedController =  errorController.updateStatus(405);
        Response notAllowedResponse = notAllowedController.createResponse(request);

        Header header = notAllowedResponse.getHeader();

        assertEquals("HTTP/1.1 405 Method Not Allowed\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("<center><h1>405</h1>Method not allowed.</center>".getBytes(), notAllowedResponse.getBody());
    }
}