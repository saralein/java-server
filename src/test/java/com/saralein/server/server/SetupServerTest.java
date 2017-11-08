package com.saralein.server.server;

import com.saralein.server.mocks.MockLogger;
import com.saralein.server.mocks.MockResponder;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.Assert.*;

public class SetupServerTest {
    MockLogger logger;
    Runtime runtime;
    MockResponder response;

    @Before
    public void setUp() {
        logger = new MockLogger();
        runtime = Runtime.getRuntime();
        response = new MockResponder();
    }

    @Test
    public void setupLogsErrorInSetup() {
        String[] args = new String[]{"6066"};

        try {
            new ServerSocket(6066);
        } catch (IOException e) {
            fail("Test failed to create blocking socket.");
        } finally {
            new SetupServer(logger, runtime, response).setup(args);
            assertEquals("Address already in use (Bind failed)", logger.getReceivedStatus());
        }
    }
    
    @Test
    public void setsUpAndReturnsNewServer() {
        String[] args = new String[]{"1337"};
        Server server = new SetupServer(logger, runtime, response).setup(args);

        assertNotNull(server);
        assertEquals(Server.class, server.getClass());
    }
}