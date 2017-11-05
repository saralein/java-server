package com.saralein.server.connection;

import java.io.IOException;

public interface IServerSocket {
    int getPort();
    Connection accept() throws IOException;
    void close() throws IOException;
}
