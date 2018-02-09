package com.saralein.server.exchange;

public class Message {
    public String summary(Header header) {
        return header.formatToString();
    }

    public byte[] full(Header header, byte[] body) {
        byte[] headerBytes = header.formatToString().getBytes();
        byte[] combined = new byte[headerBytes.length + body.length];

        for (int i = 0; i < combined.length; ++i) {
            combined[i] = i < headerBytes.length ? headerBytes[i] : body[i - headerBytes.length];
        }

        return combined;
    }
}
