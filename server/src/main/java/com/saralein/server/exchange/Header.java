package com.saralein.server.exchange;

import com.saralein.server.protocol.StatusCodes;
import java.util.HashMap;
import java.util.Map;
import static com.saralein.server.Constants.CRLF;

public class Header {
    private HashMap<String, String> header;

    public Header() {
        this.header = new HashMap<>();
    }

    public void status(int code) {
        header.put("Status", createStatusLine(code));
    }

    public Map<String, String> getHeaders() {
        return header;
    }

    public void addHeader(String title, String content) {
        header.put(title, content);
    }

    public String formatToString() {
        StringBuilder headerBuilder = new StringBuilder();

        appendStatusLine(headerBuilder);
        appendHeaders(headerBuilder);
        appendBlankLine(headerBuilder);

        return headerBuilder.toString();
    }

    private String createStatusLine(int code) {
        return "HTTP/1.1 " + StatusCodes.retrieve(code);
    }

    private void appendStatusLine(StringBuilder headerBuilder) {
        if (header.containsKey("Status")) {
            String statusLine = header.get("Status") + CRLF;
            headerBuilder.append(statusLine);
        }
    }

    private void appendHeaders(StringBuilder headerBuilder) {
        for (String key : header.keySet()) {
            if (!key.equals("Status")) {
                String headerLine = key + ": " + header.get(key) + CRLF;
                headerBuilder.append(headerLine);
            }
        }
    }

    private void appendBlankLine(StringBuilder headerBuilder) {
        if (headerBuilder.length() != 0) {
            headerBuilder.append(CRLF);
        }
    }
}
