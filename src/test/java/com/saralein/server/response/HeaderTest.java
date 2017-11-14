package com.saralein.server.response;

import static org.junit.Assert.*;
import org.junit.Test;

public class HeaderTest {
    @Test
    public void returns200HeaderForHTML() {
        String header200 = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";
        Header header = new Header("200 OK", "text/html");

        assertEquals(header200, header.createContents());
    }

    @Test
    public void returns404HeaderForGIF() {
        String header404 = "HTTP/1.1 404 Not Found\r\nContent-Type: image/gif\r\n\r\n";
        Header header = new Header("404 Not Found", "image/gif");

        assertEquals(header404, header.createContents());
    }
}