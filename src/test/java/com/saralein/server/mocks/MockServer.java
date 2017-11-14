package com.saralein.server.mocks;

import com.saralein.server.request.RequestParser;
import com.saralein.server.response.SysFileHelper;
import com.saralein.server.router.ServerRouter;
import com.saralein.server.server.Server;

public class MockServer extends Server {
    private boolean stopCalled = false;

    public MockServer() {
        super(new MockServerSocket(),
              new MockLogger(),
              new ServerRouter(new SysFileHelper("public")),
              new RequestParser());
    }

    @Override
    public void stop() {
        stopCalled = true;
    }

    public boolean stopWasCalled() {
        return stopCalled;
    }
}
