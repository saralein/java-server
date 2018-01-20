package com.saralein.cobspec.logger;

import com.saralein.server.logger.Logger;
import com.saralein.server.mocks.MockIO;
import com.saralein.server.request.Request;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ApplicationLoggerTest {
    private ByteArrayOutputStream output;
    private MockIO mockIO;
    private Logger logger;

    @Before
    public void setUp() {
        Path logTxt = Paths.get(System.getProperty("user.dir"), "src/test/test.txt");
        output = new ByteArrayOutputStream();
        mockIO = new MockIO();
        logger = new ApplicationLogger(new PrintStream(output), logTxt, mockIO);
    }

    @Test
    public void logsErrorToStreamAndIO() {
        Exception exception = new Exception("Test error");
        logger.error(exception);

        assertTrue(output.toString().contains("Test error"));
        assertTrue(mockIO.appendCalledWith().contains("Test error"));
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
    public void logsRequestTraceToStreamAndIO() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/cheetara.jpg")
                .build();
        logger.trace(request);
        String expected = "GET /cheetara.jpg HTTP/1.1";

        assertTrue(output.toString().contains(expected));
        assertTrue(mockIO.appendCalledWith().contains(expected));
    }

    @Test
    public void notifiesOfErrorInLogging() {
        mockIO.setToThrowError();
        logger.error(new Exception("Whoops."));

        assertTrue(output.toString().contains("Failed to write to log."));
    }
}
