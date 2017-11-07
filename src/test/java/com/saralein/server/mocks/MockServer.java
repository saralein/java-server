package com.saralein.server.mocks;

import com.saralein.server.server.Server;

import java.util.concurrent.ExecutorService;

public class MockServer extends Server {
    private boolean stopCalled = false;

    public MockServer() {
        super(new MockServerSocket(), new MockLogger());
    }

    @Override
    public void stop() {
        stopCalled = true;
    }

    public boolean stopWasCalled() {
        return stopCalled;
    }
}

