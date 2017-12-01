package com.saralein.server.connection;

import com.saralein.server.logger.Logger;

import java.io.IOException;

public class ListeningSocket implements ServerSocket {
    private final java.net.ServerSocket serverSocket;
    private final int port;
    private final Logger logger;

    public ListeningSocket(int port, Logger logger) throws IOException {
        this.port = port;
        this.logger = logger;
        this.serverSocket = new java.net.ServerSocket(port);
    }

    public int getPort() {
        return port;
    }

    public Connection accept() throws IOException {
        return new ConnectionSocket(serverSocket.accept(), logger);
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            logger.log(e.getMessage());
        }
    }
}
