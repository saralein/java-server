package com.saralein.server.response;

import com.saralein.server.connection.Connection;

import java.io.IOException;

public class ResponseHandler {
    public static void sendResponse(Connection socket) throws IOException {
        byte[] response = ResponseBuilder.createResponse();
        socket.write(response);
    }
}
