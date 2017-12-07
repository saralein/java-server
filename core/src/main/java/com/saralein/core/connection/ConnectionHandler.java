package com.saralein.core.connection;

import com.saralein.core.logger.Logger;
import com.saralein.core.request.Request;
import com.saralein.core.request.RequestParser;
import com.saralein.core.response.Response;
import com.saralein.core.response.ResponseSerializer;
import com.saralein.core.router.Router;

public class ConnectionHandler implements Runnable {
    private final Logger logger;
    private final Connection socket;
    private final Router router;
    private final RequestParser requestParser;
    private final ResponseSerializer responseSerializer;

    public ConnectionHandler(Connection socket, Logger logger, Router router,
                             RequestParser requestParser, ResponseSerializer responseSerializer) {
        this.socket = socket;
        this.logger = logger;
        this.router = router;
        this.requestParser = requestParser;
        this.responseSerializer = responseSerializer;
    }

    public void run() {
        try {
            Request request = requestParser.parse(socket.read());
            Response response = router.resolveRequest(request);
            socket.write(responseSerializer.convertToBytes(response));
            socket.close();
        } catch (Exception e) {
            logger.log(e.getMessage());
        }
    }
}
