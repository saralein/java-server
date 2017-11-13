package com.saralein.server.server;

import com.saralein.server.mocks.MockLogger;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.SysFileHelper;
import com.saralein.server.router.ServerRouter;
import java.io.IOException;
import java.net.ServerSocket;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SetupServerTest {
    private MockLogger logger;
    private Runtime runtime;
    private ServerRouter router;
    private RequestParser requestParser;

    @Before
    public void setUp() {
        logger = new MockLogger();
        runtime = Runtime.getRuntime();
        router = new ServerRouter(new SysFileHelper("public"));
        requestParser = new RequestParser();
    }

    @Test
    public void setupLogsErrorInSetup() {
        String[] args = new String[]{"6066"};

        try {
            new ServerSocket(6066);
        } catch (IOException e) {
            fail("Test failed to create blocking socket.");
        } finally {
            new SetupServer(logger, runtime, router, requestParser).setup(args);
            assertEquals("Address already in use (Bind failed)", logger.getReceivedStatus());
        }
    }
    
    @Test
    public void setsUpAndReturnsNewServer() {
        String[] args = new String[]{"1337"};
        Server server = new SetupServer(logger, runtime, router, requestParser).setup(args);

        assertNotNull(server);
        assertEquals(Server.class, server.getClass());
    }
}