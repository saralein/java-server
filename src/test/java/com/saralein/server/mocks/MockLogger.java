package com.saralein.server.mocks;

import com.saralein.server.logger.Logger;

public class MockLogger implements Logger {
    private String receivedStatus;

    public String getReceivedStatus() {
        return receivedStatus;
    }

    public void log(String status) {
        receivedStatus = status;
    }
}
