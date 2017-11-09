package com.saralein.server.mocks;

import com.saralein.server.response.ResponseBuilder;
import com.saralein.server.server.Server;

public class MockServer extends Server {
    private boolean stopCalled = false;

    public MockServer() {
        super(new MockServerSocket(), new MockLogger(), new ResponseBuilder());
    }

    @Override
    public void stop() {
        stopCalled = true;
    }

    public boolean stopWasCalled() {
        return stopCalled;
    }
}

