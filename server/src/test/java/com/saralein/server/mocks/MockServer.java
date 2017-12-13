package com.saralein.server.mocks;

import com.saralein.server.request.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.router.Routes;
import com.saralein.server.router.ServerRouter;
import com.saralein.server.setup.Server;

import java.nio.file.Paths;
import java.util.HashMap;

public class MockServer extends Server {
    private boolean stopCalled = false;

    public MockServer() {
        super("0.0.0.0",
              new MockServerSocket(),
              new MockLogger(),
              new ServerRouter(
                      new Routes(new HashMap<>(),
                                 new MockController(200, "Directory response"),
                                 new MockController(200, "File response"),
                                 new MockController(404, "Not found response")),
                      Paths.get(System.getProperty("user.dir") + "/" + "public")),
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
