package com.saralein.core.connection;

import java.io.IOException;

public interface ServerSocket {
    int getPort();
    Connection accept() throws IOException;
    void close() throws IOException;
}
