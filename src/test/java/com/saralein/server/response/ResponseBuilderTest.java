package com.saralein.server.response;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ResponseBuilderTest {
    private ResponseBuilder responseBuilder;

    @Before
    public void setUp() {
        responseBuilder = new ResponseBuilder();
    }

    @Test
    public void addsStatusToBuilder() {
        String status = "HTTP/1.1 200 OK\r\n\r\n";
        Response response = responseBuilder
                                .addStatus(200)
                                .build();
        Header header = response.getHeader();

        assertEquals(status, header.formatToString());
    }

    @Test
    public void addsHeaderToBuilder() {
        String fullHeader = "Location: /\r\n\r\n";
        Response response = responseBuilder
                                .addHeader("Location", "/")
                                .build();
        Header header = response.getHeader();

        assertEquals(fullHeader, header.formatToString());
    }

    @Test
    public void addsBodyToBuilder() {
        byte[] body = "Hello Builder".getBytes();
        Response response = responseBuilder
                                .addBody(body)
                                .build();

        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseWithAllPieces() {
        byte[] fullResponse = "HTTP/1.1 200 OK\r\nContent-Type: image/gif\r\n\r\nHello Builder".getBytes();
        Response response = responseBuilder
                                .addStatus(200)
                                .addHeader("Content-Type", "image/gif")
                                .addBody("Hello Builder")
                                .build();
        byte[] responseBytes = new ResponseSerializer().convertToBytes(response);

        assertArrayEquals(fullResponse, responseBytes);
    }
}