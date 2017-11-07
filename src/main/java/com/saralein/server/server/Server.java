package com.saralein.server.server;

import com.saralein.server.connection.Connection;
import com.saralein.server.connection.ConnectionHandler;
import com.saralein.server.connection.IServerSocket;
import com.saralein.server.logger.ILogger;

import java.io.IOException;

public class Server implements Runnable {
    private final IServerSocket serverSocket;
    private final ILogger logger;

    private boolean listening = true;

    public Server(IServerSocket serverSocket, ILogger logger) {
        this.serverSocket = serverSocket;
        this.logger = logger;
    }

    public void run() {
        logger.log("Server is starting..." +
                "\nServer is listening on port " + serverSocket.getPort() + "...");

        try {
            while(listening) {
                Connection socket = serverSocket.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(socket, logger);
                connectionHandler.run();
            }
            serverSocket.close();
        } catch (IOException e) {
            logger.log(e.getMessage());
        }
    }

    public void stop() {
        listening = false;
    }
}