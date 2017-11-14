package com.saralein.server;

import com.saralein.server.logger.ConnectionLogger;
import com.saralein.server.logger.Logger;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.SysFileHelper;
import com.saralein.server.router.ServerRouter;
import com.saralein.server.server.Server;
import com.saralein.server.server.SetupServer;
import com.saralein.server.validation.ArgsValidation;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        ArgsValidation argsValidation = new ArgsValidation(System.getProperty("user.home"));
        Logger logger = new ConnectionLogger(System.out);

        if (argsValidation.argsAreValid(args)) {
            Integer port = argsValidation.parsePort();
            File root = argsValidation.determineRoot();
            Runtime runtime = Runtime.getRuntime();
            SysFileHelper sysFileHelper = new SysFileHelper(root);
            ServerRouter router = new ServerRouter(sysFileHelper);
            RequestParser requestParser = new RequestParser();
            Server server = new SetupServer(logger, runtime, router, requestParser).setup(port);

            server.run();
        } else {
            logger.log("Invalid arguments: Server cannot run with given arguments.");
        }
    }
}
