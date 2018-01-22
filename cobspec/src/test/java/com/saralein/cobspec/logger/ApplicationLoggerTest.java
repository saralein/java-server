package com.saralein.cobspec.logger;

import com.saralein.server.logger.Logger;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ApplicationLoggerTest {
    private ByteArrayOutputStream output;
    private Logger logger;

    @Before
    public void setUp() {
        output = new ByteArrayOutputStream();
        logger = new ApplicationLogger(new PrintStream(output));
    }

    @Test
    public void logsErrorToStreamAndLog() {
        logger.error("Test error");

        assertTrue(output.toString().contains("Test error"));
        assertTrue(logger.retrieveLog().contains("Test error\n"));
    }

    @Test
    public void logsFatalToStream() {
        logger.fatal("System failure");

        assertTrue(output.toString().contains("System failure\n"));
    }

    @Test
    public void logsInfoToStream() {
        logger.info("I am logging!");
        logger.info("I am still logging!");
        String result = output.toString();

        assertTrue(result.contains("I am logging!\n"));
        assertTrue(result.contains("I am still logging!\n"));
    }

    @Test
    public void logsRequestTraceToStreamAndLog() {
        String message = "GET /cheetara.jpg HTTP/1.1";
        logger.trace(message);

        assertTrue(output.toString().contains(message));
        assertTrue(logger.retrieveLog().contains(message));
    }
}
