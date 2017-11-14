package com.saralein.server.server;

import com.saralein.server.connection.ListeningSocket;
import com.saralein.server.connection.ServerSocket;
import com.saralein.server.logger.Logger;
import com.saralein.server.request.RequestParser;
import com.saralein.server.router.Router;
import com.saralein.server.ShutdownHook;
import java.io.IOException;

public class SetupServer {
    private final Logger logger;
    private final Runtime runtime;
    private final Router router;
    private final RequestParser requestParser;

    public SetupServer(Logger logger, Runtime runtime, Router router, RequestParser requestParser) {
        this.logger = logger;
        this.runtime = runtime;
        this.router = router;
        this.requestParser =requestParser;
    }

    public Server setup(Integer port) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ListeningSocket(port);
        } catch (IOException e) {
            logger.log(e.getMessage());
        }

        Server server = new Server(serverSocket, logger, router, requestParser);

        runtime.addShutdownHook(new ShutdownHook(server, logger));

        return server;
    }
}
