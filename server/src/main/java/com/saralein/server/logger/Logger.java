package com.saralein.server.logger;

import com.saralein.server.request.Request;

public interface Logger {
    void info(String string);
    void exception(Exception e);
    void request(Request request);
}