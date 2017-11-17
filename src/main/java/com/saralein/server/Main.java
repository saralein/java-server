package com.saralein.server;

import com.saralein.server.logger.ConnectionLogger;
import com.saralein.server.logger.Logger;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.SysFileHelper;
import com.saralein.server.router.ServerRouter;
import com.saralein.server.server.Server;
import com.saralein.server.server.SetupServer;
import com.saralein.server.validation.ArgsValidation;
import com.saralein.server.validation.DirectoryValidator;
import com.saralein.server.validation.PortValidator;
import com.saralein.server.validation.Validator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String home = System.getProperty("user.home");
        Logger logger = new ConnectionLogger(System.out);

        List<Validator> validators = new ArrayList<Validator>(){{
            add(new PortValidator());
            add(new DirectoryValidator(home));
        }};

        List<String> validationErrors = new ArgsValidation(validators).validate(args);

        if (validationErrors.isEmpty()) {
            ArgsParser argsParser = new ArgsParser(args);
            Integer port = argsParser.parsePort();
            File root = argsParser.parseRoot(home);

            Runtime runtime = Runtime.getRuntime();
            SysFileHelper sysFileHelper = new SysFileHelper(root);
            ServerRouter router = new ServerRouter(sysFileHelper);
            RequestParser requestParser = new RequestParser();
            Server server = new SetupServer(logger, runtime, router, requestParser).setup(port);

            server.run();
        } else {
            logger.log(String.join("\n", validationErrors));
        }
    }
}
