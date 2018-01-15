package com.saralein.server;

import com.saralein.server.connection.Connection;
import com.saralein.server.connection.ConnectionHandler;
import com.saralein.server.connection.ServerSocket;
import com.saralein.server.controller.Controller;
import com.saralein.server.logger.Logger;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import java.io.IOException;

public class Server implements Runnable {
    private final ServerSocket serverSocket;
    private final Logger logger;
    private final Controller router;
    private final RequestParser requestParser;
    private final String serverIP;
    private final ResponseSerializer responseSerializer;

    private boolean listening = true;

    public Server(String serverIP, ServerSocket serverSocket, Logger logger, Controller router,
                  RequestParser requestParser, ResponseSerializer responseSerializer) {
        this.serverIP = serverIP;
        this.serverSocket = serverSocket;
        this.logger = logger;
        this.router = router;
        this.requestParser = requestParser;
        this.responseSerializer = responseSerializer;
    }

    public void run() {
        logger.info("Server is listening at http://" + serverIP + ":" + serverSocket.getPort() + "...");

        try {
            while(listening) {
                Connection socket = serverSocket.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(socket, logger, router, requestParser, responseSerializer);
                connectionHandler.run();
            }
            serverSocket.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void stop() {
        listening = false;
    }
}