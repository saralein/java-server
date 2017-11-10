package com.saralein.server.mocks;

import com.saralein.server.response.Response;

public class MockResponder implements Response {
    public byte[] createResponse() {
        String response = "HTTP/1.1 200 OK\r\n" +
                          "Content-Type: text/html\r\n\r\n" +
                          "<html><head></head><body><h1>Response from Server</h1></body></html>";

        return response.getBytes();
    }
}
