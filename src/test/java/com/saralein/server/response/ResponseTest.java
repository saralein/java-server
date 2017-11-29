package com.saralein.server.response;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResponseTest {
    Response response;
    byte[] body;

    @Before
    public void setUp() {
        Header header = new Header();
        header.addStatus(200);
        header.addHeader("Content-Type", "text/html");

        body = "Hello".getBytes();

        response = new Response(header, body);
    }

    @Test
    public void getsHeaderFromResponse() {
        Header responseHeader = response.getHeader();

        assertEquals(Header.class, responseHeader.getClass());
        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n", responseHeader.formatToString());
    }

    @Test
    public void getsBodyFromResponse() {
        assertArrayEquals(body, response.getBody());
    }
}