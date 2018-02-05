package com.saralein.cobspec.logger;

import java.io.PrintStream;

public class StreamTransport implements LogTransport {
    private final PrintStream stream;

    public StreamTransport(PrintStream stream) {
        this.stream = stream;
    }

    @Override
    public void log(String message) {
        stream.print(message);
    }
}
