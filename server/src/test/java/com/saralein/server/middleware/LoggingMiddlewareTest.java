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

    @Before
    public void setUp() {
        mockMiddleware = new MockMiddleware();
        logger = new MockLogger();
        loggingMiddleware = new LoggingMiddleware(logger);
        loggingMiddleware.apply(mockMiddleware);
    }

    @Test
    public void callingLoggingMiddlewareCallsAppliedMiddleware() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/cheetara.jpg");
        }});
        Response response = loggingMiddleware.call(request);

        assertTrue(mockMiddleware.wasCalled());
        assertArrayEquals("Middleware response".getBytes(), response.getBody());
    }

    @Test
    public void callingLoggingMiddlewareLogsRequest() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/cheetara.jpg");
        }});
        loggingMiddleware.call(request);

        assertEquals("GET /cheetara.jpg HTTP/1.1", logger.getReceivedMessage());
    }
}