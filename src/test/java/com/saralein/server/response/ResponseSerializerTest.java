package com.saralein.server.response;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResponseSerializerTest {
    private Response response = new ResponseBuilder()
                                      .addStatus(200)
                                      .addHeader("Content-Type", "text/html")
                                      .addBody("<h1>I AM A HEADER</h1>")
                                      .build();
    private String responseString = "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: text/html\r\n\r\n" +
                                    "<h1>I AM A HEADER</h1>";
    private byte[] responseBytes = responseString.getBytes();
    private ResponseSerializer responseSerializer = new ResponseSerializer();

    @Test
    public void convertsResponseHeaderAndBodyToByteArray() {
        assertArrayEquals(responseBytes, responseSerializer.convertToBytes(response));
    }
}