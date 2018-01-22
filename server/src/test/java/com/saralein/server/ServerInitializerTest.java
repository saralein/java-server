package com.saralein.server;

import com.saralein.server.controller.Controller;
import com.saralein.server.controller.ErrorController;
import com.saralein.server.mocks.MockController;
import com.saralein.server.mocks.MockLogger;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.router.Router;
import com.saralein.server.router.Routes;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class ServerInitializerTest {
    private MockLogger logger;
    private Runtime runtime;
    private Router router;
    private RequestParser requestParser;
    private ResponseSerializer responseSerializer;
    private ExecutorService thread;

    @Before
    public void setUp() {
        logger = new MockLogger();
        runtime = Runtime.getRuntime();
        Path root = Paths.get(System.getProperty("user.dir"), "public");

        Controller directoryController = new MockController(200, "Directory response");
        Controller fileController = new MockController(200, "File response");
        Controller partialContentController = new MockController(200, "Partial content response");
        ErrorController notFoundController = new ErrorController();

        Routes routes = new Routes();

        router = new Router(directoryController, fileController,
                partialContentController, notFoundController, routes, root);
        requestParser = new RequestParser();
        responseSerializer = new ResponseSerializer();
        thread = Executors.newSingleThreadExecutor();
    }

    @Test
    public void setupLogsErrorInSetup() {
        try {
            new ServerSocket(6066);
        } catch (IOException e) {
            fail("Test failed to createContents blocking socket.");
        } finally {
            new ServerInitializer(logger, runtime, router, requestParser, responseSerializer, thread).setup(6066);
            assertEquals("Address already in use (Bind failed)", logger.getReceivedMessage());
        }
    }

    @Test
    public void setsUpAndReturnsNewServer() {
        Server server = new ServerInitializer(
                logger, runtime, router, requestParser, responseSerializer, thread).setup(1337);

        assertNotNull(server);
        assertEquals(Server.class, server.getClass());
    }
}