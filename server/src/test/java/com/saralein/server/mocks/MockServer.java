package com.saralein.server.mocks;

import com.saralein.server.Application;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.Server;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

public class MockServer extends Server {
    private boolean stopCalled = false;

    public MockServer() {
        super("0.0.0.0",
              new MockServerSocket(),
              new MockLogger(),
              new Application.Builder(Paths.get(System.getProperty("user.dir"), "public")).build(),
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
