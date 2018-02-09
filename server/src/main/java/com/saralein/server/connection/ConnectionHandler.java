package com.saralein.server.connection;

import com.saralein.server.Application;
import com.saralein.server.logger.Logger;
import com.saralein.server.request.Request;
import com.saralein.server.request.parser.RequestParser;
import com.saralein.server.response.Response;
import java.io.IOException;

public class ConnectionHandler implements Runnable {
    private final Logger logger;
    private final Connection socket;
    private final Application application;
    private final RequestParser requestParser;

    public ConnectionHandler(Connection socket, Logger logger, Application application, RequestParser requestParser) {
        this.socket = socket;
        this.logger = logger;
        this.application = application;
        this.requestParser = requestParser;
    }

    @Override
    public void run() {
        try {
            Request request = requestParser.parse(socket.read());
            Response response = application.call(request);
            socket.write(response.full());
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
