package com.saralein.server.response;

import com.saralein.server.exchange.Header;

public class ResponseSerializer {
    public byte[] convertToBytes(Response response) {
        Header header = response.getHeader();
        byte[] body = response.getBody();

        byte[] headerBytes = header.formatToString().getBytes();

        if (body != null) {
            return combineResponseParts(headerBytes, body);
        } else {
            return headerBytes;
        }
    }

    private byte[] combineResponseParts(byte[] headers, byte[] body) {
        byte[] combined = new byte[headers.length + body.length];

        for (int i = 0; i < combined.length; ++i) {
            combined[i] = i < headers.length ? headers[i] : body[i - headers.length];
        }

        return combined;
    }
}
