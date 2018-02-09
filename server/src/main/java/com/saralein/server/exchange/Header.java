package com.saralein.server.exchange;

import com.saralein.server.protocol.StatusCodes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.saralein.server.Constants.CRLF;

public class Header {
    private HashMap<String, String> header;
    private List<Cookie> cookies;

    public Header() {
        this.header = new HashMap<>();
        this.cookies = new ArrayList<>();
    }

    public void status(int code) {
        header.put("Status", createStatusLine(code));
    }

    public void addHeader(String title, String content) {
        header.put(title, content);
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public String formatToString() {
        StringBuilder headerBuilder = new StringBuilder();

        appendStatusLine(headerBuilder);
        appendHeaders(headerBuilder);
        appendSetCookies(headerBuilder);
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

    private void appendSetCookies(StringBuilder headerBuilder) {
        for (Cookie cookie : cookies) {
            String cookieLine = String.format("Set-Cookie: %s", cookie.toString()) + CRLF;
            headerBuilder.append(cookieLine);
        }
    }

    private void appendBlankLine(StringBuilder headerBuilder) {
        if (headerBuilder.length() != 0) {
            headerBuilder.append(CRLF);
        }
    }
}
