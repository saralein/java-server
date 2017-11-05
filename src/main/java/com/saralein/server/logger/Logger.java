package com.saralein.server.logger;

import java.io.PrintStream;

public class Logger implements ILogger {
    private PrintStream printer;

    public Logger(PrintStream printer) {
        this.printer = printer;
    }

    public void log(String status) {
        printer.println(status);
    }
}