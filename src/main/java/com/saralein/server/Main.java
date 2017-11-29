package com.saralein.server;

import com.saralein.server.logger.ConnectionLogger;
import com.saralein.server.logger.Logger;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.response.SysFileHelper;
import com.saralein.server.router.Routes;
import com.saralein.server.router.ServerRouter;
import com.saralein.server.router.SetupRoutes;
import com.saralein.server.server.Server;
import com.saralein.server.server.SetupServer;
import com.saralein.server.validation.ArgsValidation;
import com.saralein.server.validation.DirectoryValidator;
import com.saralein.server.validation.PortValidator;
import com.saralein.server.validation.Validator;
import java.nio.file.Path;
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
            Path root = argsParser.parseRoot(home);

            Runtime runtime = Runtime.getRuntime();
            Routes routes = new SetupRoutes().setup();
            SysFileHelper sysFileHelper = new SysFileHelper(root);
            ServerRouter router = new ServerRouter(routes, sysFileHelper);
            RequestParser requestParser = new RequestParser();
            ResponseSerializer responseSerializer = new ResponseSerializer();
            Server server = new SetupServer(logger, runtime, router, requestParser, responseSerializer).setup(port);

            server.run();
        } else {
            logger.log(String.join("\n", validationErrors));
        }
    }
}
