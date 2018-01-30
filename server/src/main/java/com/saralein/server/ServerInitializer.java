package com.saralein.server;

import com.saralein.server.connection.ListeningSocket;
import com.saralein.server.connection.ServerSocket;
import com.saralein.server.logger.Logger;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.router.Router;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;

public class ServerInitializer {
    private final Logger logger;
    private final Runtime runtime;
    private final Router router;
    private final RequestParser requestParser;
    private final ResponseSerializer responseSerializer;
    private final ExecutorService threadPool;

    public ServerInitializer(Logger logger, Runtime runtime,
                             Router router, RequestParser requestParser,
                             ResponseSerializer responseSerializer, ExecutorService threadPool) {
        this.logger = logger;
        this.runtime = runtime;
        this.router = router;
        this.requestParser =requestParser;
        this.responseSerializer = responseSerializer;
        this.threadPool = threadPool;
    }

    public Server setup(Integer port) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ListeningSocket(port);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        Server server = new Server(findServerIP(), serverSocket, logger, router,
                                   requestParser, responseSerializer, threadPool);

        runtime.addShutdownHook(new ShutdownHook(server, logger));

        return server;
    }

    private String findServerIP() {
        String serverIP = null;

        try {
            InetAddress address = InetAddress.getLocalHost();
            serverIP = address.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
        }

        return serverIP;
    }
}
