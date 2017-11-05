package com.saralein.server.connection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionSocket implements Connection {
    private final Socket socket;

    public ConnectionSocket(Socket socket) {
        this.socket = socket;
    }

    public void write(byte[] output) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.write(output);
        out.flush();
        out.close();
    }

    public void close() throws IOException {
        socket.close();
    }
}
