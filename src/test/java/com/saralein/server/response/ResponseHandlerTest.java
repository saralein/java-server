package com.saralein.server.response;

import com.saralein.server.mocks.MockSocket;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ResponseHandlerTest {
    private MockSocket socket;
    private byte[] responseArray;

    @Before
    public void setUp() {
        socket = new MockSocket();

        String response = "HTTP/1.1 200 OK\r\n" +
                          "Content-Type: text/html\r\n\r\n" +
                          "<html><head></head><body><h1>Response from Server</h1></body></html>";
        responseArray = response.getBytes();
    }

    @Test
    public void sendsResponseToSocket() {
        try {
            ResponseHandler.sendResponse(socket);
            assertArrayEquals(responseArray, socket.getResponseReceived());
        } catch (IOException e) {
            fail("Failed to send response to connection in ResponseTest.");
        }
    }
}
