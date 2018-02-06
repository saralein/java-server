package com.saralein.cobspec.logger;

import com.saralein.cobspec.data.LogStore;

public class StoreTransport implements LogTransport {
    private final LogStore logStore;

    public StoreTransport(LogStore logStore) {
        this.logStore = logStore;
    }

    @Override
    public void log(String message) {
        logStore.add(message);
    }
}
