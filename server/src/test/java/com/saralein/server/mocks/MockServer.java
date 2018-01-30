package com.saralein.server.mocks;

import com.saralein.server.Server;
import com.saralein.server.controller.ErrorController;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.router.Router;
import com.saralein.server.router.Routes;

import java.nio.file.Paths;
import java.util.concurrent.Executors;

public class MockServer extends Server {
    private boolean stopCalled = false;

    public MockServer() {
        super("0.0.0.0",
              new MockServerSocket(),
              new MockLogger(),
              new Router(
                      new MockController(200, "Directory response"),
                      new MockController(200, "File response"),
                      new ErrorController(),
                      new Routes(),
                      Paths.get(System.getProperty("user.dir") + "/" + "public")),
              new RequestParser(),
              new ResponseSerializer(),
              Executors.newSingleThreadExecutor());
    }

    @Override
    public void stop() {
        stopCalled = true;
    }

    public boolean stopWasCalled() {
        return stopCalled;
    }
}
