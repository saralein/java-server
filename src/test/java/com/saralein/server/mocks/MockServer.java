package com.saralein.server.mocks;

import com.saralein.server.request.RequestParser;
import com.saralein.server.response.SysFileHelper;
import com.saralein.server.router.ServerRouter;
import com.saralein.server.server.Server;
import java.io.File;

public class MockServer extends Server {
    private boolean stopCalled = false;

    public MockServer() {
        super("0.0.0.0",
              new MockServerSocket(),
              new MockLogger(),
              new ServerRouter(
                      new SysFileHelper(
                              new File(System.getProperty("user.dir") + File.separator + "public"))),
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
