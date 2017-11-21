package com.saralein.server.controller;

import static org.junit.Assert.*;

import com.saralein.server.Controller.NotFoundResponse;
import org.junit.Before;
import org.junit.Test;

public class NotFoundResponseTest {
    private byte[] responseArray;
    private NotFoundResponse notFoundResponse;

    @Before
    public void setUp() {
        String response = "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/html\r\n\r\n" +
                "<center><h1>404</h1>Page not found.</center>";

        responseArray = response.getBytes();

        notFoundResponse = new NotFoundResponse();
    }

    @Test
    public void returnsResponseFor404() {
        assertArrayEquals(responseArray, notFoundResponse.createResponse());
    }
}