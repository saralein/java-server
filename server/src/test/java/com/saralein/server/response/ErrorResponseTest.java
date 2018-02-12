package com.saralein.server.response;

import com.saralein.server.exchange.Header;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ErrorResponseTest {
    @Test
    public void creates404Response() {
        Response response = new ErrorResponse(404).respond();
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("404 Not Found".getBytes(), response.getBody());
    }

    @Test
    public void creates405Response() {
        Response response = new ErrorResponse(405).respond();
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 405 Method Not Allowed\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("405 Method Not Allowed".getBytes(), response.getBody());
    }

    @Test
    public void creates416Response() {
        Response response = new ErrorResponse(416).respond("Content-Range", "*/100");
        Header header = response.getHeader();
        String expected = "HTTP/1.1 416 Range Not Satisfiable\r\n" +
                "Content-Range: */100\r\nContent-Type: text/html\r\n\r\n";

        assertEquals(expected, header.formatToString());
        assertArrayEquals("416 Range Not Satisfiable".getBytes(), response.getBody());
    }
}
