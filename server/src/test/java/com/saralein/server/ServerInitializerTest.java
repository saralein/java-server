package com.saralein.server;

import com.saralein.server.mocks.MockCallable;
import com.saralein.server.mocks.MockLogger;
import com.saralein.server.request.parser.HeaderParser;
import com.saralein.server.request.parser.RequestLineParser;
import com.saralein.server.request.parser.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ServerInitializerTest {
    private MockLogger logger;
    private Runtime runtime;
    private Application application;
    private RequestParser requestParser;
    private ResponseSerializer responseSerializer;
    private ExecutorService thread;

    @Before
    public void setUp() {
        logger = new MockLogger();
        runtime = Runtime.getRuntime();
        application = new Application(new MockCallable());
        requestParser = new RequestParser(new RequestLineParser(), new HeaderParser());
        responseSerializer = new ResponseSerializer();
        thread = Executors.newSingleThreadExecutor();
    }

    @Test
    public void setupLogsErrorInSetup() throws IOException {
        new ServerSocket(6066);
        new ServerInitializer(logger, runtime, application, requestParser, responseSerializer, thread).setup(6066);

        assertEquals("Address already in use (Bind failed)", logger.getReceivedMessage());
    }

    @Test
    public void setsUpAndReturnsNewServer() {
        Server server = new ServerInitializer(
                logger, runtime, application, requestParser, responseSerializer, thread).setup(1337);

        assertNotNull(server);
        assertEquals(Server.class, server.getClass());
    }
}
