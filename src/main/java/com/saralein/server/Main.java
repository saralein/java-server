package com.saralein.server;

import com.saralein.server.server.Server;
import com.saralein.server.server.SetupServer;

public class Main {
    public static void main(String[] args) {
        Server server = SetupServer.setup(args);
        server.run();
    }
}
