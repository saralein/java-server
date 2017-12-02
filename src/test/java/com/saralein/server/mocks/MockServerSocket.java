package com.saralein.server.mocks;

import com.saralein.server.connection.ServerSocket;
import com.saralein.server.connection.Connection;
import java.io.IOException;

public class MockServerSocket implements ServerSocket {
    public int getPort() { return 1337; }

    public Connection accept() throws IOException {
        return new MockSocket();
    }

    public void close() throws IOException {}
}
