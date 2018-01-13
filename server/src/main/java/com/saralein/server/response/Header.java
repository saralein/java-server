package com.saralein.server.response;

import static com.saralein.server.Constants.CRLF;
import com.saralein.server.protocol.StatusCodes;
import java.util.HashMap;

public class Header {
    private HashMap<String, String> header;

    public Header() {
        this.header = new HashMap<>();
    }

    public void addStatus(int code) {
        header.put("Status", createStatusLine(code));
    }

    public void addHeader(String title, String content) {
        header.put(title, content);
    }

    public String formatToString() {
        StringBuilder headerBuilder = new StringBuilder();

        addsStatusLine(headerBuilder);
        addsHeaders(headerBuilder);
        addsBlankLine(headerBuilder);

        return headerBuilder.toString();
    }

    private String createStatusLine(int code) {
        return  "HTTP/1.1 " + StatusCodes.retrieve(code);
    }

    private void addsStatusLine(StringBuilder headerBuilder) {
        if (header.containsKey("Status")) {
            String statusLine = header.get("Status") + CRLF;
            headerBuilder.append(statusLine);
        }
    }

    private void addsHeaders(StringBuilder headerBuilder) {
        for (String key : header.keySet()) {
            if (!key.equals("Status")) {
                String headerLine = key + ": " + header.get(key) + CRLF;
                headerBuilder.append(headerLine);
            }
        }
    }

    private void addsBlankLine(StringBuilder headerBuilder) {
        if (headerBuilder.length() != 0) {
            headerBuilder.append(CRLF);
        }
    }
}
