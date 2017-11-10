package com.saralein.server;

import com.saralein.server.logger.ConnectionLogger;
import com.saralein.server.logger.Logger;
import com.saralein.server.response.DirectoryResponse;
import com.saralein.server.response.SysFileHelper;
import com.saralein.server.server.Server;
import com.saralein.server.server.SetupServer;

public class Main {
    public static void main(String[] args) {
        Logger logger = new ConnectionLogger(System.out);
        Runtime runtime = Runtime.getRuntime();
        SysFileHelper sysFileHelper = new SysFileHelper("public");
        DirectoryResponse directoryResponse = new DirectoryResponse(sysFileHelper);

        Server server = new SetupServer(logger, runtime, directoryResponse).setup(args);
        server.run();
    }
}
