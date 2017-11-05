package com.saralein.server;

import com.saralein.server.mocks.MockLogger;
import com.saralein.server.mocks.MockServer;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class ShutdownHookTest {
    ShutdownHook shutdownHook;
    MockServer server;
    ExecutorService pool;

    @Before
    public void setUp() {
        pool = Executors.newSingleThreadExecutor();
        server = new MockServer(pool);
        MockLogger logger = new MockLogger();
        shutdownHook = new ShutdownHook(server, pool, logger);
    }

    @Test
    public void shutdownHookClosesServerSocketAndPool() {
        assertFalse(server.stopWasCalled());

        shutdownHook.run();

        assertTrue(server.stopWasCalled());
    }
}
