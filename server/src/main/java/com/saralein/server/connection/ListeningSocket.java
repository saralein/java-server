package com.saralein.server.connection;

import java.io.IOException;

public class ListeningSocket implements ServerSocket {
    private final java.net.ServerSocket serverSocket;
    private final int port;

    public ListeningSocket(int port) throws IOException {
        this.port = port;
        this.serverSocket = new java.net.ServerSocket(port);
    }

    public int getPort() {
        return port;
    }

    public Connection accept() throws IOException {
        return new ConnectionSocket(serverSocket.accept());
    }

    public void close() throws IOException {
        serverSocket.close();
    }
}
