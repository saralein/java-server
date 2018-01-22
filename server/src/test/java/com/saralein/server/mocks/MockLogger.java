package com.saralein.server.mocks;

import com.saralein.server.logger.Logger;

public class MockLogger implements Logger {
    private String received;

    public String getReceivedMessage() {
        return received;
    }

    @Override
    public void error(String error) {
        received = error;
    }

    @Override
    public void fatal(String message) {
        received = message;
    }

    @Override
    public void info(String message) {
        received = message;
    }

    @Override
    public void trace(String message) {
        received = message;
    }

    @Override
    public String retrieveLog() {
        return received;
    }
}
