package com.saralein.cobspec.controller;

import com.saralein.server.exchange.Header;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CoffeeControllerTest {
    @Test
    public void returns418ResponseWithBody() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("coffee")
                .build();
        Response response = new CoffeeController().call(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 418 I'm a teapot\r\nContent-Type: text/html\r\n\r\n";

        assertEquals(expected, header.formatToString());
        assertArrayEquals("418 I'm a teapot".getBytes(), response.getBody());
    }
}
