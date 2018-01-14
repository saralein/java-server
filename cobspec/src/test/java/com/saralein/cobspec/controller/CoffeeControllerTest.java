package com.saralein.cobspec.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoffeeControllerTest {
    @Test
    public void returns418ResponseWithBody() {
        Request request = new Request.Builder()
                .addMethod("GET")
                .addUri("coffee")
                .build();
        Response response = new CoffeeController().createResponse(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 418 I'm a teapot\r\nContent-type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("I'm a teapot.".getBytes(), response.getBody());
    }
}