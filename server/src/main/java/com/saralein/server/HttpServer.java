package com.saralein.server;

import com.saralein.server.logger.Logger;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.router.Routes;
import com.saralein.server.router.ServerRouter;
import com.saralein.server.setup.Server;
import com.saralein.server.setup.SetupServer;
import java.nio.file.Path;

public class HttpServer {
    public static void start(int port, Path root, Routes routes, Logger logger) {
        Runtime runtime = Runtime.getRuntime();
        ServerRouter router = new ServerRouter(routes, root);
        RequestParser requestParser = new RequestParser();
        ResponseSerializer responseSerializer = new ResponseSerializer();
        Server server = new SetupServer(logger, runtime, router, requestParser, responseSerializer).setup(port);

        server.run();
    }
}