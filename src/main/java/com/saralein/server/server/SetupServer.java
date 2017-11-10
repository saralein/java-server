package com.saralein.server.server;

import com.saralein.server.ShutdownHook;
import com.saralein.server.connection.ListeningSocket;
import com.saralein.server.connection.ServerSocket;
import com.saralein.server.logger.Logger;
import com.saralein.server.response.Response;

import java.io.IOException;

public class SetupServer {
    private final Logger logger;
    private final Runtime runtime;
    private final Response response;

    private int getPort(String[] args) {
        return Integer.parseInt(args[0]);
    }

    public SetupServer(Logger logger, Runtime runtime, Response response) {
        this.logger = logger;
        this.runtime = runtime;
        this.response = response;
    }

    public Server setup(String[] args) {
        Integer port = getPort(args);
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ListeningSocket(port);
        } catch (IOException e) {
            logger.log(e.getMessage());
        }

        Server server = new Server(serverSocket, logger, response);

        runtime.addShutdownHook(new ShutdownHook(server, logger));

        return server;
    }
}
