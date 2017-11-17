package com.saralein.server.connection;

import static com.saralein.server.Constants.CRLF;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnectionSocket implements Connection {
    private final Socket socket;

    ConnectionSocket(Socket socket) {
        this.socket = socket;
    }

    public String read() throws IOException {
        StringBuilder request = new StringBuilder();

        InputStreamReader in = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(in);

        String str;

        while ((str = bufferedReader.readLine()) != null && !str.isEmpty()) {
            request.append(str).append(CRLF);
        }

        return request.toString();
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
