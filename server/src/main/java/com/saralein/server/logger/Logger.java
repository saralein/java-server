package com.saralein.server.logger;

import com.saralein.server.request.Request;

public interface Logger {
    void error(Exception e);
    void fatal(String message);
    void info(String message);
    void trace(Request request);
}
