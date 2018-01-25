package com.saralein.cobspec.logger;

import com.saralein.cobspec.data.LogStore;
import com.saralein.server.logger.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;

public class ApplicationLoggerTest {
    private ByteArrayOutputStream output;
    private LogStore logStore;
    private Logger logger;

    @Before
    public void setUp() {
        output = new ByteArrayOutputStream();
        logStore = new LogStore();
        logger = new ApplicationLogger(new PrintStream(output), logStore);
    }

    @Test
    public void logsErrorToStreamAndLog() {
        logger.error("Test error");

        assertTrue(output.toString().contains("Test error\n"));
        assertTrue(logStore.retrieveLog().contains("Test error\n"));
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
}
