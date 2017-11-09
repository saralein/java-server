package com.saralein.server.connection;

import com.saralein.server.logger.Logger;
import com.saralein.server.response.Response;

import java.io.IOException;

public class ConnectionHandler implements Runnable {
    private final Logger logger;
    private final Connection socket;
    private final Response responseBuilder;

    public ConnectionHandler(Connection socket, Logger logger, Response responseBuilder) {
        this.socket = socket;
        this.logger = logger;
        this.responseBuilder = responseBuilder;
    }

    public void sendResponse(Connection socket) throws IOException {
        byte[] response = responseBuilder.createResponse();
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
