package com.saralein.server.response;

import org.junit.Test;

import static org.junit.Assert.*;

public class HeaderTest {
    private String header200 = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";
    private String header404 = "HTTP/1.1 404 Not Found\r\nContent-Type: image/gif\r\n\r\n";

    @Test
    public void returns200HeaderForHTML() {
        Header header = new Header("200 OK", "text/html");

        assertEquals(header200, header.getContent());
    }

    @Test
    public void returns404HeaderForGIF() {
        Header header = new Header("404 Not Found", "image/gif");

        assertEquals(header404, header.getContent());
    }
}