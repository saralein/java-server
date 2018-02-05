package com.saralein.cobspec.mocks;

import com.saralein.cobspec.logger.LogTransport;

public class MockTransport implements LogTransport {
    private String calledWith;

    public MockTransport() {
        this.calledWith = "";
    }

    @Override
    public void log(String message) {
        calledWith = message;
    }

    public boolean contains(String message) {
        return calledWith.contains(message);
    }
}
