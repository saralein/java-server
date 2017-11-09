package com.saralein.server;

import com.saralein.server.logger.ConnectionLogger;
import com.saralein.server.logger.Logger;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;
import com.saralein.server.server.Server;
import com.saralein.server.server.SetupServer;

public class Main {
    public static void main(String[] args) {
        Logger logger = new ConnectionLogger(System.out);
        Runtime runtime = Runtime.getRuntime();
        Response responseBuilder = new ResponseBuilder();

        Server server = new SetupServer(logger, runtime, responseBuilder).setup(args);
        server.run();
    }
}
