package com.saralein.cobspec.data;

import org.junit.Test;
import static org.junit.Assert.*;

public class LogTest {
    @Test
    public void addLogMessagesToLog() {
        LogStore logStore = new Log();
        logStore.add("Message 1\n");
        logStore.add("Message 2\n");

        assertEquals("Message 1\nMessage 2\n", logStore.retrieveLog());
    }
}