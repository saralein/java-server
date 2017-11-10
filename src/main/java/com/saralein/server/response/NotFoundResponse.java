package com.saralein.server.response;

import static com.saralein.server.Constants.STATUS_CODES;

public class NotFoundResponse implements Response {
    private final String contentType = "text/html";

    public byte[] createResponse() {
        StringBuilder response = new StringBuilder();
        response.append(createHeader());
        response.append(createBody());

        return response.toString().getBytes();
    }

    private String createHeader() {
        return new Header(STATUS_CODES.get("404"), contentType).getContent();
    }

    private String createBody() {
        return "<center><h1>404</h1>Page not found.</center>";
    }
}