package com.saralein.server.server;

import com.saralein.server.connection.ConnectionHandler;
import com.saralein.server.connection.IServerSocket;
import com.saralein.server.logger.ILogger;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class Server implements Runnable {
    private final IServerSocket serverSocket;
    private final ILogger logger;
    private final ExecutorService pool;

    private boolean listening = true;

    public Server(IServerSocket serverSocket, ILogger logger, ExecutorService pool) {
        this.serverSocket = serverSocket;
        this.logger = logger;
        this.pool = pool;
    }

    public void run() {
        logger.log("Server is starting..." +
                "\nServer is listening on port " + serverSocket.getPort() + "...");

        try {
            while(listening) {
                pool.submit(new ConnectionHandler(serverSocket.accept(), logger));
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