package com.saralein.server.response;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResponseTest {
    Response response;

    @Before
    public void setUp() {
        Header header = new Header();
        header.addStatus(200);
        header.addHeader("Content-Type", "text/html");

        byte[] body = "Hello".getBytes();

        response = new Response(header, body);
    }

    @Test
    public void convertsResponseToBytes() {
        byte[] fullResponse = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\nHello".getBytes();

        assertArrayEquals(fullResponse, response.convertToBytes());
    }
}