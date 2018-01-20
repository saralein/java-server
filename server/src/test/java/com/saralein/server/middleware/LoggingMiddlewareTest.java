package com.saralein.server.middleware;

import com.saralein.server.mocks.MockLogger;
import com.saralein.server.mocks.MockMiddleware;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoggingMiddlewareTest {
    private MockMiddleware mockMiddleware;
    private LoggingMiddleware loggingMiddleware;
    private MockLogger logger;
    private Request request;

    @Before
    public void setUp() {
        mockMiddleware = new MockMiddleware();
        logger = new MockLogger();
        loggingMiddleware = new LoggingMiddleware(logger);
        loggingMiddleware.apply(mockMiddleware);
        request = new Request.Builder()
                .method("GET")
                .uri("/cheetara.jpg")
                .build();
    }

    @Test
    public void callingLoggingMiddlewareCallsAppliedMiddleware() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/cheetara.jpg")
                .build();
        Response response = loggingMiddleware.call(request);

        assertTrue(mockMiddleware.wasCalled());
        assertArrayEquals("Middleware response".getBytes(), response.getBody());
    }

    @Test
    public void callingLoggingMiddlewareLogsRequest() {
        loggingMiddleware.call(request);

        assertEquals("GET /cheetara.jpg HTTP/1.1", logger.getReceivedMessage());
    }
}