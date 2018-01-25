package com.saralein.cobspec.data;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LogStoreTest {
    @Test
    public void addLogMessagesToLog() {
        LogStore logStore = new LogStore();
        logStore.add("Message 1\n");
        logStore.add("Message 2\n");

        assertEquals("Message 1\nMessage 2\n", logStore.retrieveLog());
    }
}