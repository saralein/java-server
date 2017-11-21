package com.saralein.server.server;

import com.saralein.server.mocks.MockLogger;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.SysFileHelper;
import com.saralein.server.router.Routes;
import com.saralein.server.router.ServerRouter;
import java.io.File;
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
        String rootPath = System.getProperty("user.dir") + "/" + "public";
        File root = new File(rootPath);
        router = new ServerRouter(new Routes(), new SysFileHelper(root));
        requestParser = new RequestParser();
    }

    @Test
    public void setupLogsErrorInSetup() {
        try {
            new ServerSocket(6066);
        } catch (IOException e) {
            fail("Test failed to createContents blocking socket.");
        } finally {
            new SetupServer(logger, runtime, router, requestParser).setup(6066);
            assertEquals("Address already in use (Bind failed)", logger.getReceivedStatus());
        }
    }

    @Test
    public void setsUpAndReturnsNewServer() {
        Server server = new SetupServer(logger, runtime, router, requestParser).setup(1337);

        assertNotNull(server);
        assertEquals(Server.class, server.getClass());
    }
}