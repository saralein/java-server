package com.saralein.server.connection;

import com.saralein.server.logger.ILogger;
import com.saralein.server.response.ResponseHandler;

import java.io.IOException;

public class ConnectionHandler implements Runnable {
    private final ILogger logger;
    private final Connection socket;

    public ConnectionHandler(Connection socket, ILogger logger) {
        this.socket = socket;
        this.logger = logger;
    }

    public void run() {
        synchronized (this) {
            try {
                ResponseHandler.sendResponse(socket);
                socket.close();
            } catch (IOException e) {
                logger.log(e.getMessage());
            }
        }
    }
}
