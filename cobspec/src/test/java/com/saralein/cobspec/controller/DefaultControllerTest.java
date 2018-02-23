package com.saralein.cobspec.controller;

import com.saralein.server.exchange.Header;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DefaultControllerTest {
    @Test
    public void returns200Response() {
        Request request = new Request.Builder().build();
        DefaultController defaultController = new DefaultController();
        Response response = defaultController.call(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
        assertArrayEquals("".getBytes(), response.getBody());
    }
}
