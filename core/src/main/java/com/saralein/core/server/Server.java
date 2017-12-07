package com.saralein.core.server;

import com.saralein.core.connection.Connection;
import com.saralein.core.connection.ConnectionHandler;
import com.saralein.core.connection.ServerSocket;
import com.saralein.core.logger.Logger;
import com.saralein.core.request.RequestParser;
import com.saralein.core.response.ResponseSerializer;
import com.saralein.core.router.Router;
import java.io.IOException;

public class Server implements Runnable {
    private final ServerSocket serverSocket;
    private final Logger logger;
    private final Router router;
    private final RequestParser requestParser;
    private final String serverIP;
    private final ResponseSerializer responseSerializer;

    private boolean listening = true;

    public Server(String serverIP, ServerSocket serverSocket, Logger logger, Router router,
                  RequestParser requestParser, ResponseSerializer responseSerializer) {
        this.serverIP = serverIP;
        this.serverSocket = serverSocket;
        this.logger = logger;
        this.router = router;
        this.requestParser = requestParser;
        this.responseSerializer = responseSerializer;
    }

    public void run() {
        logger.log("Server is starting..." +
                "\nServer is listening at http://" + serverIP + ":" + serverSocket.getPort() + "...");

        try {
            while(listening) {
                Connection socket = serverSocket.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(socket, logger, router, requestParser, responseSerializer);
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