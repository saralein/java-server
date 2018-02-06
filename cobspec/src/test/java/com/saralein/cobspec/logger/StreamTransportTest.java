package com.saralein.cobspec.logger;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static junit.framework.TestCase.assertTrue;

public class StreamTransportTest {
    private ByteArrayOutputStream output;
    private StreamTransport streamTransport;

    @Before
    public void setUp() {
        output = new ByteArrayOutputStream();
        streamTransport = new StreamTransport(new PrintStream(output));
    }

    @Test
    public void logsErrorToStreamAndLog() {
        streamTransport.log("Test error\n");
        streamTransport.log("Another error");

        assertTrue(output.toString().contains("Test error\nAnother error"));
    }
}