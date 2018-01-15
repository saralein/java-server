package com.saralein.server.mocks;

import com.saralein.server.logger.Logger;
import com.saralein.server.request.Request;

public class MockLogger implements Logger {
    private String received;

    public String getReceivedMessage() {
        return received;
    }

    public void exception(Exception e) {
        received = e.getMessage();
    }

    public void info(String info) {
        received = info;
    }

    public void request(Request request) {
        received = request.getRequestLine();
    }
}
