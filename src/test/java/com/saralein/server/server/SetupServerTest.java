package com.saralein.server.server;

import com.saralein.server.Controller.DirectoryController;
import com.saralein.server.Controller.FileController;
import com.saralein.server.Controller.NotFoundController;
import com.saralein.server.mocks.MockLogger;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.FileHelper;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.response.SysFileHelper;
import com.saralein.server.router.Routes;
import com.saralein.server.router.RoutesBuilder;
import com.saralein.server.router.ServerRouter;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        FileHelper fileHelper = new SysFileHelper(root);
        DirectoryController directoryController = new DirectoryController(fileHelper);
        FileController fileController = new FileController(fileHelper);
        NotFoundController notFoundController = new NotFoundController();

        Routes routes = new RoutesBuilder(directoryController, fileController, notFoundController).build();

        router = new ServerRouter(routes, new SysFileHelper(root));
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