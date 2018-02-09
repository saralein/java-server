package com.saralein.server.response;

import com.saralein.server.exchange.Header;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ResponseTest {
    private Response response;
    private byte[] body;

    @Before
    public void setUp() {
        Header header = new Header();
        header.status(200);
        header.addHeader("Content-Type", "text/html");

        body = "Hello".getBytes();

        response = new Response(header, body);
    }

    @Test
    public void addsStatusToResponse() {
        String status = "HTTP/1.1 200 OK\r\n\r\n";
        Response response = new Response.Builder()
                .status(200)
                .build();
        Header header = response.getHeader();

        assertEquals(status, header.formatToString());
    }

    @Test
    public void addsHeaderToResponse() {
        String fullHeader = "Location: /\r\n\r\n";
        Response response = new Response.Builder()
                .addHeader("Location", "/")
                .build();
        Header header = response.getHeader();

        assertEquals(fullHeader, header.formatToString());
    }

    @Test
    public void addsBodyToResponse() {
        byte[] body = "Hello Builder".getBytes();
        Response response = new Response.Builder()
                .body(body)
                .build();

        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void createsResponseWithAllPieces() {
        byte[] fullResponse = "HTTP/1.1 200 OK\r\nContent-Type: image/gif\r\n\r\nHello Builder".getBytes();
        Response response = new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "image/gif")
                .body("Hello Builder")
                .build();
        byte[] responseBytes = new ResponseSerializer().convertToBytes(response);

        assertArrayEquals(fullResponse, responseBytes);
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