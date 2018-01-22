package com.saralein.cobspec.logger;

import com.saralein.cobspec.data.LogStore;
import com.saralein.server.logger.Logger;

import java.io.PrintStream;
import java.time.Instant;

public class ApplicationLogger implements Logger {
    private PrintStream printer;
    private LogStore logStore;

    public ApplicationLogger(PrintStream printer, LogStore logStore) {
        this.printer = printer;
        this.logStore = logStore;
    }

    @Override
    public void error(String error) {
        String message = formatMessage("ERROR", error);
        logStore.add(message);
        printer.print(message);
    }

    @Override
    public void fatal(String message) {
        printer.print(formatMessage("FATAL", message));
    }

    @Override
    public void info(String message) {
        printer.print(formatMessage("INFO", message));
    }

    @Override
    public void trace(String message) {
        String formattedMessage = formatMessage("TRACE", message);
        logStore.add(formattedMessage);
        printer.print(formattedMessage);
    }

    private String formatMessage(String category, String message) {
        return String.format("%s [%s] %s\n", category, Instant.now(), message);
    }
}
