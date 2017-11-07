package com.saralein.server.server;

import com.saralein.server.ShutdownHook;
import com.saralein.server.connection.DefaultServerSocket;
import com.saralein.server.connection.IServerSocket;
import com.saralein.server.logger.ILogger;
import com.saralein.server.logger.Logger;

import java.io.IOException;

public class SetupServer {
    private static int getPort(String[] args) {
        return Integer.parseInt(args[0]);
    }


    public static Server setup(String[] args) {
        Integer port = getPort(args);
        ILogger logger = new Logger(System.out);
        IServerSocket serverSocket = null;

        try {
            serverSocket = new DefaultServerSocket(port);
        } catch (IOException e) {
            logger.log(e.getMessage());
        }

        Server server = new Server(serverSocket, logger);

        Runtime.getRuntime().addShutdownHook(new ShutdownHook(server, logger));

        return server;
    }
}
