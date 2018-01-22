package com.saralein.server;

import com.saralein.server.mocks.MockLogger;
import com.saralein.server.mocks.MockMiddleware;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.router.Routes;
import com.saralein.server.router.Router;
import java.io.IOException;
import java.net.ServerSocket;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ServerInitializerTest {
    private MockLogger logger;
    private Runtime runtime;
    private Application application;
    private RequestParser requestParser;
    private ResponseSerializer responseSerializer;

    @Before
    public void setUp() {
        logger = new MockLogger();
        runtime = Runtime.getRuntime();
        application = new Application(new MockMiddleware());
        requestParser = new RequestParser();
        responseSerializer = new ResponseSerializer();
    }

    @Test
    public void setupLogsErrorInSetup() {
        try {
            new ServerSocket(6066);
        } catch (IOException e) {
            fail("Test failed to createContents blocking socket.");
        } finally {
            new ServerInitializer(logger, runtime, application, requestParser, responseSerializer).setup(6066);
            assertEquals("Address already in use (Bind failed)", logger.getReceivedMessage());
        }
    }

    @Test
    public void setsUpAndReturnsNewServer() {
        Server server = new ServerInitializer(logger, runtime, application, requestParser, responseSerializer).setup(1337);

        assertNotNull(server);
        assertEquals(Server.class, server.getClass());
    }
}