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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AppLoggerTest {
    private Path logTxt;
    private ByteArrayOutputStream output;
    private Logger logger;

    @Before
    public void setUp() {
        logTxt = Paths.get(System.getProperty("user.dir") + "/src/test/logger-test.txt");
        output = new ByteArrayOutputStream();
        logger = new AppLogger(new PrintStream(output), logTxt);
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
    public void logsExceptionsToStream() {
        output.reset();
        Exception exception = new Exception("Test exception");
        logger.exception(exception);

        assertTrue(output.toString().contains("Test exception"));
    }

    @Test
    public void logsExceptionsToFile() throws IOException {
        output.reset();
        Exception exception = new Exception("Writing exception");
        Files.write(logTxt, "".getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        logger.exception(exception);
        String logged = String.join("", Files.readAllLines(logTxt));

        assertTrue(logged.contains("Writing exception"));
    }

    @Test
    public void logsRequestsToStream() {
        output.reset();
        Request request = new Request.Builder()
                .addMethod("GET")
                .addUri("/cheetara.jpg")
                .build();
        logger.request(request);

        assertTrue(output.toString().contains("GET /cheetara.jpg HTTP/1.1"));
    }

    @Test
    public void logsRequestToFile() throws IOException {
        output.reset();
        Request request = new Request.Builder()
                .addMethod("GET")
                .addUri("/snarf.jpg")
                .build();
        Files.write(logTxt, "".getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        logger.request(request);
        String logged = String.join("", Files.readAllLines(logTxt));

        assertTrue(logged.contains("GET /snarf.jpg HTTP/1.1"));
    }
}