package com.saralein.server.connection;

import java.io.IOException;
import java.net.ServerSocket;

public class DefaultServerSocket implements IServerSocket {
    private final ServerSocket serverSocket;
    private final int port;

    public DefaultServerSocket(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
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
