package com.saralein.core.logger;

import java.io.PrintStream;

public class ConnectionLogger implements Logger {
    private PrintStream printer;

    public ConnectionLogger(PrintStream printer) {
        this.printer = printer;
    }

    public void log(String status) {
        printer.println(status);
    }
}