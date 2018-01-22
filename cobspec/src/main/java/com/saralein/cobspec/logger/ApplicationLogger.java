package com.saralein.cobspec.logger;

import com.saralein.server.filesystem.FileIO;
import com.saralein.server.logger.Logger;
import com.saralein.server.request.Request;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.Instant;

public class ApplicationLogger implements Logger {
    private final Path log;
    private final FileIO fileIO;
    private PrintStream printer;

    public ApplicationLogger(PrintStream printer, Path log, FileIO fileIO) {
        this.printer = printer;
        this.log = log;
        this.fileIO = fileIO;
    }

    public void error(Exception e) {
        String message = formatMessage("ERROR", e.getMessage());
        appendToLog(message);
        printer.print(message);
    }

    public void fatal(String message) {
        printer.print(formatMessage("FATAL", message));
    }

    public void info(String message) {
        printer.print(formatMessage("INFO", message));
    }

    public void trace(Request request) {
        String message = formatMessage("TRACE", request.getRequestLine());
        appendToLog(message);
        printer.print(message);
    }

    private String formatMessage(String category, String message) {
        return String.format("%s [%s] %s\n", category, Instant.now(), message);
    }

    private void appendToLog(String message) {
        try {
            fileIO.appendFile(log, message);
        } catch (IOException e) {
            info("Failed to write to log.");
        }
    }
}
