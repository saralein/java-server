package com.saralein.server.mocks;

import com.saralein.server.Application;
import com.saralein.server.Server;
import com.saralein.server.request.parser.*;
import com.saralein.server.response.ResponseSerializer;
import java.util.concurrent.Executors;

public class MockServer extends Server {
    private boolean stopCalled = false;

    public MockServer() {
        super("0.0.0.0",
                new MockServerSocket(),
                new MockLogger(),
                new Application(new MockCallable()),
                new RequestParser(
                        new RequestLineParser(),
                        new HeaderParser(),
                        new ParameterParser(),
                        new CookieParser()),
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
