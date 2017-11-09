package com.saralein.server.connection;

import java.io.IOException;

public interface Connection {
    void write(byte[] output) throws IOException;
    void close() throws IOException;
}
