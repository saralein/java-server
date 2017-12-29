package com.saralein.cobspec.logger;

import com.saralein.server.logger.Logger;
import com.saralein.server.request.Request;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;

public class AppLogger implements Logger {
    private final Path log;
    private PrintStream printer;

    public AppLogger(PrintStream printer, Path log) {
        this.printer = printer;
        this.log = log;
    }

    public void exception(Exception e) {
        String message = formatMessage("EXCEPTION", e.getMessage());
        writeToFile(message);
        printer.print(message);
    }

    public void info(String info) {
        printer.print(formatMessage("INFO", info));
    }

    public void request(Request request) {
        String message = formatMessage("REQUEST", request.getRequestLine());
        writeToFile(message);
        printer.print(message);
    }

    private String formatMessage(String category, String message) {
        return String.format("%s [%s] %s\n", category, Instant.now(), message);
    }

    private void writeToFile(String message) {
        try {
            if (!Files.exists(log)) {
                Files.createFile(log);
            }

            Files.write(log, message.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            info("Failed to write to log.");
        }
    }
}