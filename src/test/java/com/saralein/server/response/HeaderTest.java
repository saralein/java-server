package com.saralein.server.response;

import static org.junit.Assert.*;
import org.junit.Test;

public class HeaderTest {
    @Test
    public void addsStatusToHeader() {
        String header200 = "HTTP/1.1 200 OK\r\n\r\n";
        Header header = new Header();

        header.addStatus(200);

        assertArrayEquals(header200.getBytes(), header.convertToBytes());
    }

    @Test
    public void returns200HeaderForHTML() {
        String header200 = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";
        Header header = new Header();

        header.addStatus(200);
        header.addHeader("Content-Type", "text/html");

        assertArrayEquals(header200.getBytes(), header.convertToBytes());
    }

    @Test
    public void returns404HeaderForGIF() {
        String header404 = "HTTP/1.1 404 Not Found\r\nContent-Type: image/gif\r\n\r\n";
        Header header = new Header();

        header.addStatus(404);
        header.addHeader("Content-Type", "image/gif");

        assertArrayEquals(header404.getBytes(), header.convertToBytes());
    }
}