package com.saralein.server.connection;

import com.saralein.server.mocks.MockLogger;
import com.saralein.server.mocks.MockResponder;
import com.saralein.server.mocks.MockSocket;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ConnectionHandlerTest {
    private MockSocket socket;
    private ConnectionHandler connectionHandler;
    private byte[] responseArray;

    @Before
    public void setUp() {
        String response = "HTTP/1.1 200 OK\r\n" +
                          "Content-Type: text/html\r\n\r\n" +
                          "<html><head></head><body><h1>Response from Server</h1></body></html>";

        responseArray = response.getBytes();

        MockLogger logger = new MockLogger();
        socket = new MockSocket();
        MockResponder responder = new MockResponder();
        connectionHandler = new ConnectionHandler(socket, logger, responder);
    }

    @Test
    public void sendsResponseToSocket() {
        try {
            connectionHandler.sendResponse(socket);
            assertArrayEquals(responseArray, socket.getResponseReceived());
        } catch (IOException e) {
            fail("Failed to send response to socket.");
        }
    }
}