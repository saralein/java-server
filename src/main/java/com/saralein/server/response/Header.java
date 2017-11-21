package com.saralein.server.response;

import static com.saralein.server.Constants.CRLF;
import static com.saralein.server.Constants.STATUS_CODES;
import java.util.HashMap;

public class Header {
    private final String version = "HTTP/1.1";
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

    public byte[] convertToBytes() {
        return createContents().getBytes();
    }

    private String createStatusLine(int code) {
        return  version + " " + STATUS_CODES.get(code);
    }

    private String createContents() {
        StringBuilder headerBuilder = new StringBuilder();

        if (header.containsKey("Status")) {
            headerBuilder.append(header.get("Status") + CRLF);
        }

        for (String key : header.keySet()) {
            if (!key.equals("Status")) {
                headerBuilder.append(key + ": " + header.get(key) + CRLF);
            }
        }

        if (headerBuilder.length() != 0) {
            headerBuilder.append(CRLF);
        }

        return headerBuilder.toString();
    }
}
