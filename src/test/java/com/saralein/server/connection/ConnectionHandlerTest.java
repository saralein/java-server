package com.saralein.server.connection;

import com.saralein.server.mocks.MockLogger;
import com.saralein.server.mocks.MockSocket;
import org.junit.Before;
import org.junit.Test;

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
        connectionHandler = new ConnectionHandler(socket, logger);
    }

    @Test
    public void sendsResponseToSocket() {
        connectionHandler.run();
        assertArrayEquals(responseArray, socket.getResponseReceived());
    }
}
