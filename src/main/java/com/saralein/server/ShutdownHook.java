package com.saralein.server;

import com.saralein.server.logger.ILogger;
import com.saralein.server.server.Server;

import java.util.concurrent.ExecutorService;

public class ShutdownHook extends Thread {
    private final Server server;
    private final ExecutorService pool;
    private final ILogger logger;

    public ShutdownHook(Server server, ExecutorService pool, ILogger logger) {
        this.server = server;
        this.pool = pool;
        this.logger = logger;
    }

    @Override
    public void run() {
        logger.log("\nServer is shutting down...");
        server.stop();
        pool.shutdown();
    }
}
