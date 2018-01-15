package com.saralein.server.connection;

import com.saralein.server.controller.Controller;
import com.saralein.server.logger.Logger;
import com.saralein.server.request.Request;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseSerializer;

public class ConnectionHandler implements Runnable {
    private final Logger logger;
    private final Connection socket;
    private final Controller application;
    private final RequestParser requestParser;
    private final ResponseSerializer responseSerializer;

    public ConnectionHandler(Connection socket,
                             Logger logger,
                             Controller application,
                             RequestParser requestParser,
                             ResponseSerializer responseSerializer) {
        this.socket = socket;
        this.logger = logger;
        this.application = application;
        this.requestParser = requestParser;
        this.responseSerializer = responseSerializer;
    }

    public void run() {
        try {
            Request request = requestParser.parse(socket.read());
            logger.trace(request);
            Response response = application.createResponse(request);
            socket.write(responseSerializer.convertToBytes(response));
            socket.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
