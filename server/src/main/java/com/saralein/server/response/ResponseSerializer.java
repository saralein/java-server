package com.saralein.server.response;

import com.saralein.server.exchange.Cookie;
import com.saralein.server.protocol.StatusCodes;
import java.util.List;
import java.util.Map;
import static com.saralein.server.Constants.CRLF;

public class ResponseSerializer {
    public byte[] convertToBytes(Response response) {
        byte[] headerBytes = formatResponseHeader(response);
        byte[] body = response.getBody();

        if (body != null) {
            return combineResponseParts(headerBytes, body);
        } else {
            return headerBytes;
        }
    }

    private byte[] formatResponseHeader(Response response) {
        String responseHeader = formatStatusLine(response) +
                formatHeaders(response) +
                formatCookies(response) +
                CRLF;

        return responseHeader.getBytes();
    }

    private String formatStatusLine(Response response) {
        int status = response.getStatus();
        return "HTTP/1.1 " + StatusCodes.retrieve(status) + CRLF;
    }

    private String formatHeaders(Response response) {
        StringBuilder builder = new StringBuilder();
        Map<String, String> headerFields = response.getHeaders();

        for (String key : headerFields.keySet()) {
            String header = key + ": " + headerFields.get(key);
            builder.append(header);
            builder.append(CRLF);
        }

        return builder.toString();
    }

    private String formatCookies(Response response) {
        StringBuilder builder = new StringBuilder();
        List<Cookie> cookies = response.getCookies();

        for (Cookie cookie : cookies) {
            String header = "Set-Cookie: " + cookie.getName() + "=" + cookie.getValue();
            builder.append(header);
            builder.append(CRLF);
        }

        return builder.toString();
    }

    private byte[] combineResponseParts(byte[] headers, byte[] body) {
        byte[] combined = new byte[headers.length + body.length];

        for (int i = 0; i < combined.length; ++i) {
            combined[i] = i < headers.length ? headers[i] : body[i - headers.length];
        }

        return combined;
    }
}
