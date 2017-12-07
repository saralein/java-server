package com.saralein.core.response;

import static com.saralein.core.Constants.CRLF;
import com.saralein.core.protocol.StatusCodes;
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

    public String formatToString() {
        StringBuilder headerBuilder = new StringBuilder();

        addsStatusLine(headerBuilder);
        addsHeaders(headerBuilder);
        addsBlankLine(headerBuilder);

        return headerBuilder.toString();
    }

    private String createStatusLine(int code) {
        return  version + " " + StatusCodes.retrieve(code);
    }

    private void addsStatusLine(StringBuilder headerBuilder) {
        if (header.containsKey("Status")) {
            headerBuilder.append(header.get("Status") + CRLF);
        }
    }

    private void addsHeaders(StringBuilder headerBuilder) {
        for (String key : header.keySet()) {
            if (!key.equals("Status")) {
                headerBuilder.append(key + ": " + header.get(key) + CRLF);
            }
        }
    }

    private void addsBlankLine(StringBuilder headerBuilder) {
        if (headerBuilder.length() != 0) {
            headerBuilder.append(CRLF);
        }
    }
}
