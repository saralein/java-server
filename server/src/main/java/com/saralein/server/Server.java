package com.saralein.server;

import com.saralein.server.connection.Connection;
import com.saralein.server.connection.ConnectionHandler;
import com.saralein.server.connection.ServerSocket;
import com.saralein.server.logger.Logger;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.router.Router;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class Server {
    private final ServerSocket serverSocket;
    private final Logger logger;
    private final Router router;
    private final RequestParser requestParser;
    private final String serverIP;
    private final ResponseSerializer responseSerializer;
    private final ExecutorService threadPool;
    private boolean listening;

    public Server(String serverIP, ServerSocket serverSocket,
                  Logger logger, Router router, RequestParser requestParser,
                  ResponseSerializer responseSerializer, ExecutorService threadPool) {
        this.serverIP = serverIP;
        this.serverSocket = serverSocket;
        this.logger = logger;
        this.router = router;
        this.requestParser = requestParser;
        this.responseSerializer = responseSerializer;
        this.threadPool = threadPool;
        this.listening = true;
    }

    public void run() {
        logger.info("Server is listening at http://" + serverIP + ":" + serverSocket.getPort() + "...");

        try {
            while(listening) {
                Connection socket = serverSocket.accept();
                threadPool.execute(
                        new ConnectionHandler(socket, logger, router, requestParser, responseSerializer)
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