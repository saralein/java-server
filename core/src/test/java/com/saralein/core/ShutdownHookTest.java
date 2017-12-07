package com.saralein.core;

import com.saralein.core.mocks.MockLogger;
import com.saralein.core.mocks.MockServer;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ShutdownHookTest {
    private ShutdownHook shutdownHook;
    private MockServer server;

    @Before
    public void setUp() {
        server = new MockServer();
        MockLogger logger = new MockLogger();
        shutdownHook = new ShutdownHook(server, logger);
    }

    @Test
    public void shutdownHookClosesServerSocketAndPool() {
        assertFalse(server.stopWasCalled());

        shutdownHook.run();

        assertTrue(server.stopWasCalled());
    }
}
