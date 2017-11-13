package com.saralein.server.logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;
import org.junit.Test;

public class LoggerTest {
    @Test
    public void loggerLogsStatusFromPrintStream() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Logger logger = new ConnectionLogger(new PrintStream(output));
        logger.log("I am logging!");
        logger.log("I am still logging!");
        assertEquals("I am logging!\nI am still logging!\n", output.toString());
    }
}