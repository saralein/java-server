package com.saralein.server.connection;

import com.saralein.server.logger.Logger;
import com.saralein.server.request.Request;
import com.saralein.server.request.RequestParser;
import com.saralein.server.router.Router;
import java.io.IOException;
import java.util.HashMap;

public class ConnectionHandler implements Runnable {
    private final Logger logger;
    private final Connection socket;
    private final Router router;
    private final RequestParser requestParser;

    public ConnectionHandler(Connection socket, Logger logger, Router router, RequestParser requestParser) {
        this.socket = socket;
        this.logger = logger;
        this.router = router;
        this.requestParser = requestParser;
    }

    public void run() {
        try {
            HashMap<String, String> parsedRequest = requestParser.parse(socket.read());
            Request request = new Request(parsedRequest);
            byte[] response = router.resolveRequest(request);
            socket.write(response);
            socket.close();
        } catch (IOException e) {
            logger.log(e.getMessage());
        }
    }
}
