package com.saralein.server.setup;

import com.saralein.server.controller.Controller;
import com.saralein.server.controller.ErrorController;
import com.saralein.server.mocks.MockController;
import com.saralein.server.mocks.MockErrorController;
import com.saralein.server.mocks.MockLogger;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.router.Routes;
import com.saralein.server.router.ServerRouter;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SetupServerTest {
    private MockLogger logger;
    private Runtime runtime;
    private ServerRouter router;
    private RequestParser requestParser;
    private ResponseSerializer responseSerializer;

    @Before
    public void setUp() {
        logger = new MockLogger();
        runtime = Runtime.getRuntime();
        String rootPath = System.getProperty("user.dir") + "/" + "public";
        Path root = Paths.get(rootPath);

        Controller directoryController = new MockController(200, "Directory response");
        Controller fileController = new MockController(200, "File response");
        ErrorController notFoundController = new MockErrorController(404, "Not found response");

        Routes routes = new Routes(new HashMap<>(), directoryController, fileController, notFoundController);

        router = new ServerRouter(routes, root);
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
            new SetupServer(logger, runtime, router, requestParser, responseSerializer).setup(6066);
            assertEquals("Address already in use (Bind failed)", logger.getReceivedStatus());
        }
    }

    @Test
    public void setsUpAndReturnsNewServer() {
        Server server = new SetupServer(logger, runtime, router, requestParser, responseSerializer).setup(1337);

        assertNotNull(server);
        assertEquals(Server.class, server.getClass());
    }
}