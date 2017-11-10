package com.saralein.server.server;

import com.saralein.server.connection.Connection;
import com.saralein.server.connection.ConnectionHandler;
import com.saralein.server.connection.ServerSocket;
import com.saralein.server.logger.Logger;
import com.saralein.server.response.Response;

import java.io.IOException;

public class Server implements Runnable {
    private final ServerSocket serverSocket;
    private final Logger logger;
    private final Response response;

    private boolean listening = true;

    public Server(ServerSocket serverSocket, Logger logger, Response response) {
        this.serverSocket = serverSocket;
        this.logger = logger;
        this.response = response;
    }

    public void run() {
        logger.log("Server is starting..." +
                "\nServer is listening on port " + serverSocket.getPort() + "...");

        try {
            while(listening) {
                Connection socket = serverSocket.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(socket, logger, response);
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