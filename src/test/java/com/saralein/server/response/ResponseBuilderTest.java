package com.saralein.server.response;

        import org.junit.Before;
        import org.junit.Test;

        import static org.junit.Assert.*;

public class ResponseBuilderTest {
    private ResponseBuilder responseBuilder;

    @Before
    public void setUp() {
        responseBuilder = new ResponseBuilder();
    }

    @Test
    public void addsStatusToBuilder() {
        byte[] status = "HTTP/1.1 200 OK\r\n\r\n".getBytes();
        Response response = responseBuilder
                                .addStatus(200)
                                .build();

        assertArrayEquals(status, response.convertToBytes());
    }

    @Test
    public void addsHeaderToBuilder() {
        byte[] header = "Location: /\r\n\r\n".getBytes();
        Response response = responseBuilder
                                .addHeader("Location", "/")
                                .build();

        assertArrayEquals(header, response.convertToBytes());
    }

    @Test
    public void addsBodyToBuilder() {
        byte[] body = "Hello Builder".getBytes();
        Response response = responseBuilder
                                .addBody(body)
                                .build();

        assertArrayEquals(body, response.convertToBytes());
    }

    @Test
    public void returnsResponseWithAllPieces() {
        byte[] fullResponse = "HTTP/1.1 200 OK\r\nContent-Type: image/gif\r\n\r\nHello Builder".getBytes();
        Response response = responseBuilder
                                .addStatus(200)
                                .addHeader("Content-Type", "image/gif")
                                .addBody("Hello Builder")
                                .build();

        assertArrayEquals(fullResponse, response.convertToBytes());
    }
}