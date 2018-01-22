package com.saralein.cobspec.logger;

import com.saralein.server.logger.Logger;
import java.io.PrintStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ApplicationLogger implements Logger {
    private PrintStream printer;
    private List<String> log;

    public ApplicationLogger(PrintStream printer) {
        this.printer = printer;
        this.log = new ArrayList<>();
    }

    @Override
    public void error(String error) {
        String message = formatMessage("ERROR", error);
        log.add(message);
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
        log.add(formattedMessage);
        printer.print(formattedMessage);
    }

    @Override
    public String retrieveLog() {
        return String.join("", log);
    }

    private String formatMessage(String category, String message) {
        return String.format("%s [%s] %s\n", category, Instant.now(), message);
    }
}
