package com.saralein.server.mocks;

import com.saralein.server.connection.ServerSocket;
import com.saralein.server.connection.Connection;

import java.io.IOException;

public class MockServerSocket implements ServerSocket {
    private boolean acceptCalled;
    private boolean closeCalled;

    public MockServerSocket() {
        this.acceptCalled = false;
        this.closeCalled = false;
    }

    public boolean acceptWasCalled() {
        return acceptCalled;
    }

    public boolean closeWasCalled() {
        return closeCalled;
    }

    public int getPort() { return 1337; }

    public Connection accept() throws IOException {
        acceptCalled = true;
        return new MockSocket();
    }

    public void close() throws IOException {
        closeCalled = true;
    }
}
