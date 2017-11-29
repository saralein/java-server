package com.saralein.server.mocks;

import com.saralein.server.request.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.response.SysFileHelper;
import com.saralein.server.router.Routes;
import com.saralein.server.router.ServerRouter;
import com.saralein.server.server.Server;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

public class MockServer extends Server {
    private boolean stopCalled = false;

    public MockServer() {
        super("0.0.0.0",
              new MockServerSocket(),
              new MockLogger(),
              new ServerRouter(
                      new Routes(),
                      new SysFileHelper(
                              Paths.get(System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + "public"))),
              new RequestParser(),
              new ResponseSerializer());
    }

    @Override
    public void stop() {
        stopCalled = true;
    }

    public boolean stopWasCalled() {
        return stopCalled;
    }
}
