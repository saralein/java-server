package com.saralein.server.connection;

import com.saralein.server.logger.Logger;
import com.saralein.server.response.ResponseBuilder;

import java.io.IOException;

public class ConnectionHandler implements Runnable {
    private final Logger logger;
    private final Connection socket;

    public ConnectionHandler(Connection socket, Logger logger) {
        this.socket = socket;
        this.logger = logger;
    }

    public void sendResponse(Connection socket) throws IOException {
        byte[] response = ResponseBuilder.createResponse();
        socket.write(response);
    }

    public void run() {
        try {
            sendResponse(socket);
            socket.close();
        } catch (IOException e) {
            logger.log(e.getMessage());
        }
    }
}
