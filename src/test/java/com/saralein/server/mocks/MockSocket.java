package com.saralein.server.mocks;

import com.saralein.server.connection.Connection;

import java.io.IOException;

public class MockSocket implements Connection {
    private byte[] response = null;

    public byte[] getResponseReceived() { return response; }

    public void write(byte[] output) {
        response = output;
    }

    public void close() throws IOException { }
}
