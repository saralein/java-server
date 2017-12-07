package com.saralein.core.mocks;

import com.saralein.core.logger.Logger;

public class MockLogger implements Logger {
    private String receivedStatus;

    public String getReceivedStatus() {
        return receivedStatus;
    }

    public void log(String status) {
        receivedStatus = status;
    }
}
