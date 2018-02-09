package com.saralein.server;

import com.saralein.server.connection.Connection;
import com.saralein.server.connection.ConnectionHandler;
import com.saralein.server.connection.ServerSocket;
import com.saralein.server.logger.Logger;
import com.saralein.server.request.parser.RequestParser;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class Server {
    private final ServerSocket serverSocket;
    private final Logger logger;
    private final Application application;
    private final RequestParser requestParser;
    private final String serverIP;
    private final ExecutorService threadPool;
    private boolean listening;

    public Server(String serverIP, ServerSocket serverSocket, Logger logger, Application application,
                  RequestParser requestParser, ExecutorService threadPool) {
        this.serverIP = serverIP;
        this.serverSocket = serverSocket;
        this.logger = logger;
        this.application = application;
        this.requestParser = requestParser;
        this.threadPool = threadPool;
        this.listening = true;
    }

    public void run() {
        logger.info("Server is listening at http://" + serverIP + ":" + serverSocket.getPort() + "...");

        try {
            while(listening) {
                Connection socket = serverSocket.accept();
                threadPool.execute(
                        new ConnectionHandler(socket, logger, application, requestParser)
                );
            }
            serverSocket.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void stop() {
        listening = false;
    }
}