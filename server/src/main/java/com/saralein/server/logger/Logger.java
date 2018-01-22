package com.saralein.server.logger;

public interface Logger {
    void error(String error);
    void fatal(String message);
    void info(String message);
    void trace(String message);
    String retrieveLog();
}
