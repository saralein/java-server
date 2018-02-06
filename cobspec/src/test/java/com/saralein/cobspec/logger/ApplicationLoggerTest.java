package com.saralein.cobspec.logger;

import com.saralein.cobspec.mocks.MockTransport;
import com.saralein.server.logger.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class ApplicationLoggerTest {
    private Logger logger;
    private MockTransport streamTransport;
    private MockTransport storeTransport;

    @Before
    public void setUp() {
        streamTransport = new MockTransport();
        storeTransport = new MockTransport();
        logger = new ApplicationLogger()
                .add(streamTransport)
                .add(storeTransport);
    }

    @Test
    public void logsErrorToTransports() {
        logger.error("Test error");

        assertTrue(streamTransport.contains("ERROR Test error\n"));
        assertTrue(storeTransport.contains("ERROR Test error\n"));
    }

    @Test
    public void logsFatalToTransports() {
        logger.fatal("System failure");

        assertTrue(streamTransport.contains("FATAL System failure\n"));
        assertTrue(storeTransport.contains("FATAL System failure\n"));
    }

    @Test
    public void logsInfoTransports() {
        logger.info("I am logging!");

        assertTrue(streamTransport.contains("INFO I am logging!\n"));
        assertTrue(storeTransport.contains("INFO I am logging!\n"));
    }

    @Test
    public void logsRequestTraceToTransports() {
        logger.trace("GET /cheetara.jpg HTTP/1.1");

        assertTrue(streamTransport.contains("TRACE GET /cheetara.jpg HTTP/1.1"));
        assertTrue(storeTransport.contains("TRACE GET /cheetara.jpg HTTP/1.1"));
    }
}
