package com.saralein.server.response;

import static com.saralein.server.Constants.CRLF;

public class Header {
    private final String status;
    private final String contentType;
    private final String fullContent;

    public Header(String status, String contentType) {
        this.status = status;
        this.contentType = contentType;
        this.fullContent = createHeaders();
    }

    public String getContent() {
        return fullContent;
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
}
