package com.saralein.server;

import com.saralein.server.logger.ConnectionLogger;
import com.saralein.server.logger.Logger;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.SysFileHelper;
import com.saralein.server.router.ServerRouter;
import com.saralein.server.server.Server;
import com.saralein.server.server.SetupServer;

public class Main {
    public static void main(String[] args) {
        Logger logger = new ConnectionLogger(System.out);
        Runtime runtime = Runtime.getRuntime();

        SysFileHelper sysFileHelper = new SysFileHelper("public");
        ServerRouter router = new ServerRouter(sysFileHelper);

        RequestParser requestParser = new RequestParser();

        Server server = new SetupServer(logger, runtime, router, requestParser).setup(args);
        server.run();
    }
}
