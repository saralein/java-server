package com.saralein.server.connection;

import com.saralein.server.Application;
import com.saralein.server.logger.Logger;
import com.saralein.server.request.Request;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseSerializer;
import java.io.IOException;

public class ConnectionHandler implements Runnable {
    private final Logger logger;
    private final Connection socket;
    private final Application application;
    private final RequestParser requestParser;
    private final ResponseSerializer responseSerializer;

    public ConnectionHandler(Connection socket, Logger logger, Application application,
                             RequestParser requestParser, ResponseSerializer responseSerializer) {
        this.socket = socket;
        this.logger = logger;
        this.application = application;
        this.requestParser = requestParser;
        this.responseSerializer = responseSerializer;
    }

    @Override
    public void run() {
        try {
            Request request = requestParser.parse(socket.read());
            Response response = application.call(request);
            socket.write(responseSerializer.convertToBytes(response));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            closeSocket();
        }
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
