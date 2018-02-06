package com.saralein.cobspec.logger;

import com.saralein.server.logger.Logger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ApplicationLogger implements Logger {
    private List<LogTransport> transports;

    public ApplicationLogger() {
        this.transports = new ArrayList<>();
    }

    public ApplicationLogger add(LogTransport transport) {
        transports.add(transport);
        return this;
    }

    @Override
    public void error(String error) {
        log(formatMessage("ERROR", error));
    }

    @Override
    public void fatal(String message) {
        log(formatMessage("FATAL", message));
    }

    @Override
    public void info(String message) {
        log(formatMessage("INFO", message));
    }

    @Override
    public void trace(String message) {
        log(formatMessage("TRACE", message));
    }

    private String formatMessage(String category, String message) {
        return String.format("[%s] %s %s\n", Instant.now().toString(), category, message);
    }

    private void log(String message) {
        transports.forEach(transport -> transport.log(message));
    }
}
