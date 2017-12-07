package com.saralein.core.server;

import com.saralein.core.connection.ListeningSocket;
import com.saralein.core.connection.ServerSocket;
import com.saralein.core.logger.Logger;
import com.saralein.core.request.RequestParser;
import com.saralein.core.response.ResponseSerializer;
import com.saralein.core.router.Router;
import com.saralein.core.ShutdownHook;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SetupServer {
    private final Logger logger;
    private final Runtime runtime;
    private final Router router;
    private final RequestParser requestParser;
    private final ResponseSerializer responseSerializer;

    public SetupServer(Logger logger, Runtime runtime, Router router,
                       RequestParser requestParser, ResponseSerializer responseSerializer) {
        this.logger = logger;
        this.runtime = runtime;
        this.router = router;
        this.requestParser =requestParser;
        this.responseSerializer = responseSerializer;
    }

    public Server setup(Integer port) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ListeningSocket(port);
        } catch (IOException e) {
            logger.log(e.getMessage());
        }

        Server server = new Server(findServerIP(), serverSocket, logger, router,
                                   requestParser, responseSerializer);

        runtime.addShutdownHook(new ShutdownHook(server, logger));

        return server;
    }

    private String findServerIP() {
        String serverIP = null;

        try {
            InetAddress address = InetAddress.getLocalHost();
            serverIP = address.getHostAddress();
        } catch (UnknownHostException e) {
            logger.log(e.getMessage());
        }

        return serverIP;
    }
}
