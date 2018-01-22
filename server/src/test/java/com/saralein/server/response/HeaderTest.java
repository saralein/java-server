package com.saralein.server.response;

import org.junit.Test;
import static org.junit.Assert.*;

public class HeaderTest {
    @Test
    public void addsStatusToHeader() {
        String header200 = "HTTP/1.1 200 OK\r\n\r\n";
        Header header = new Header();

        header.status(200);

        assertEquals(header200, header.formatToString());
    }

    @Test
    public void returns200HeaderForHTML() {
        String header200 = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";
        Header header = new Header();

        header.status(200);
        header.addHeader("Content-Type", "text/html");

        assertEquals(header200, header.formatToString());
    }

    @Test
    public void returns404HeaderForGIF() {
        String header404 = "HTTP/1.1 404 Not Found\r\nContent-Type: image/gif\r\n\r\n";
        Header header = new Header();

        header.status(404);
        header.addHeader("Content-Type", "image/gif");

        assertEquals(header404, header.formatToString());
    }
}