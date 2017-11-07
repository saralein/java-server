package com.saralein.server.response;

public class ResponseBuilder implements Response {
    private final String CRLF = "\r\n";

    public byte[] createResponse() {
        StringBuilder response = new StringBuilder();
        response.append(createStatusLine());
        response.append(createHeaders());
        response.append(createBody());

        return response.toString().getBytes();
    }

    private String createStatusLine() {
        return "HTTP/1.1 200 OK" + CRLF;
    }

    private String createHeaders() {
        StringBuilder headers = new StringBuilder();
        headers.append("Content-Type: text/html" + CRLF);
        headers.append(CRLF);

        return headers.toString();
    }

    private String createBody() {
        return "<html><head></head><body><h1>Response from Server</h1></body></html>";
    }
}
