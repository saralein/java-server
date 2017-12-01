package com.saralein.server.mocks;

import com.saralein.server.connection.Connection;

import java.io.*;

public class MockSocket implements Connection {
    private byte[] response = null;
    private String request = null;

    public byte[] getResponseReceived() { return response; }

    public void setRequest(String suppliedRequest) {
        request = suppliedRequest;
    }

    public String read() {
        return request;
    }

    public void write(byte[] output) {
        response = output;
    }

    public void close() throws IOException { }
}
