package com.saralein.server.server;

import com.saralein.server.ShutdownHook;
import com.saralein.server.connection.ListeningSocket;
import com.saralein.server.connection.ServerSocket;
import com.saralein.server.logger.Logger;
import com.saralein.server.logger.ConnectionLogger;

import java.io.IOException;

public class SetupServer {
    private static int getPort(String[] args) {
        return Integer.parseInt(args[0]);
    }


    public static Server setup(String[] args) {
        Integer port = getPort(args);
        Logger logger = new ConnectionLogger(System.out);
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ListeningSocket(port);
        } catch (IOException e) {
            logger.log(e.getMessage());
        }

        Server server = new Server(serverSocket, logger);

        Runtime.getRuntime().addShutdownHook(new ShutdownHook(server, logger));

        return server;
    }
}
