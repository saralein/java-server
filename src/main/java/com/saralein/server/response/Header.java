package com.saralein.server.response;

public class Header {
    private final String status;
    private final String contentType;
    private final String fullContent;
    private final String CRLF = "\r\n";

    public Header(String status, String contentType) {
        this.status = status;
        this.contentType = contentType;
        this.fullContent = createHeaders();
    }

    private String createStatusLine(String status) {
        return  "HTTP/1.1 " + status + CRLF;
    }

    private String createContentTypeHeader(String contentType) {
        return "Content-Type: " + contentType + CRLF;
    }

    private String createHeaders() {
        StringBuilder headerBuilder = new StringBuilder();

        headerBuilder.append(createStatusLine(status));
        headerBuilder.append(createContentTypeHeader(contentType));
        headerBuilder.append(CRLF);

        return headerBuilder.toString();
    }

    public String getContent() {
        return fullContent;
    }
}
