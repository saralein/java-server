package com.saralein.core.connection;

import java.io.IOException;

public interface Connection {
    String read() throws IOException;
    void write(byte[] output) throws IOException;
    void close() throws IOException;
}
