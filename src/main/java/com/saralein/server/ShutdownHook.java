package com.saralein.server;

import com.saralein.server.logger.Logger;
import com.saralein.server.server.Server;

public class ShutdownHook extends Thread {
    private final Server server;
    private final Logger logger;

    public ShutdownHook(Server server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Override
    public void run() {
        logger.log("\nServer is shutting down...");
        server.stop();
    }
}
