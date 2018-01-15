package com.saralein.cobspec.logger;

import com.saralein.server.logger.Logger;
import com.saralein.server.request.Request;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ApplicationLoggerTest {
    private Path logTxt;
    private ByteArrayOutputStream output;
    private Logger logger;

    @Before
    public void setUp() {
        logTxt = Paths.get(System.getProperty("user.dir") + "/src/test/logger-test.txt");
        output = new ByteArrayOutputStream();
        logger = new ApplicationLogger(new PrintStream(output), logTxt);
    }

    @Test
    public void logsExceptionsToStream() {
        output.reset();
        Exception exception = new Exception("Test error");
        logger.error(exception);

        assertTrue(output.toString().contains("Test error"));
    }

    @Test
    public void logsExceptionsToFile() throws IOException {
        output.reset();
        Exception exception = new Exception("Writing error");
        Files.write(logTxt, "".getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        logger.error(exception);
        String logged = String.join("", Files.readAllLines(logTxt));

        assertTrue(logged.contains("Writing error"));
    }

    @Test
    public void logsFatalToStream() {
        logger.fatal("System failure");
        String result = output.toString();

        assertTrue(result.contains("System failure\n"));
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
    public void logsRequestTraceToStream() {
        output.reset();
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/cheetara.jpg");
            put("version", "HTTP/1.1");
        }});
        logger.trace(request);

        assertTrue(output.toString().contains("GET /cheetara.jpg HTTP/1.1"));
    }

    @Test
    public void logsRequestTraceToFile() throws IOException {
        output.reset();
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/snarf.jpg");
            put("version", "HTTP/1.1");
        }});
        Files.write(logTxt, "".getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        logger.trace(request);
        String logged = String.join("", Files.readAllLines(logTxt));

        assertTrue(logged.contains("GET /snarf.jpg HTTP/1.1"));
    }
}