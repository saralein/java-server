package com.saralein.server.mocks;

import com.saralein.server.logger.Logger;
import com.saralein.server.request.Request;

public class MockLogger implements Logger {
    private String received;

    public String getReceivedMessage() {
        return received;
    }

    public void error(Exception e) {
        received = e.getMessage();
    }

    public void fatal(String message) {
        received = message;
    }

    public void info(String message) {
        received = message;
    }

    public void trace(Request request) {
        received = request.getRequestLine();
    }
}
