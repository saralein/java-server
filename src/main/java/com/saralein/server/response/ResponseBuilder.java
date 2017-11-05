package com.saralein.server.response;

public class ResponseBuilder {
    private static String CRLF = "\r\n";

    public static byte[] createResponse() {
        StringBuilder response = new StringBuilder();
        response.append(createStatusLine());
        response.append(createHeaders());
        response.append(createBody());

        return response.toString().getBytes();
    }

    private static String createStatusLine() {
        return "HTTP/1.1 200 OK" + CRLF;
    }

    private static String createHeaders() {
        StringBuilder headers = new StringBuilder();
        headers.append("Content-Type: text/html" + CRLF);
        headers.append(CRLF);

        return headers.toString();
    }

    private static String createBody() {
        return "<html><head></head><body><h1>Response from Server</h1></body></html>";
    }
}
