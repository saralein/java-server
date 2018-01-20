package com.saralein.cobspec.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultControllerTest {
    @Test
    public void returns200Response() {
        Request request = new Request.Builder().build();
        DefaultController defaultController = new DefaultController();
        Response response = defaultController.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
        assertArrayEquals("".getBytes(), response.getBody());
    }
}