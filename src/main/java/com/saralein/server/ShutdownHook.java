package com.saralein.server;

import com.saralein.server.logger.ILogger;
import com.saralein.server.server.Server;

public class ShutdownHook extends Thread {
    private final Server server;
    private final ILogger logger;

    public ShutdownHook(Server server, ILogger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Override
    public void run() {
        logger.log("\nServer is shutting down...");
        server.stop();
    }
}
