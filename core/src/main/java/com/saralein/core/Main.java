package com.saralein.core;

import com.saralein.core.logger.ConnectionLogger;
import com.saralein.core.logger.Logger;
import com.saralein.core.request.RequestParser;
import com.saralein.core.response.ResponseSerializer;
import com.saralein.core.router.Routes;
import com.saralein.core.router.ServerRouter;
import com.saralein.core.server.Server;
import com.saralein.core.server.SetupServer;
import com.saralein.core.validation.ArgsValidation;
import com.saralein.core.validation.DirectoryValidator;
import com.saralein.core.validation.PortValidator;
import com.saralein.core.validation.Validator;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Main {
    public static void main(String[] args, String home, Function<Path, Routes> setupRoutes) {
        Logger logger = new ConnectionLogger(System.out);

        List<String> validationErrors = runValidationAndReturnErrors(args, home);

        if (validationErrors.isEmpty()) {
            setupAndRunServer(args, home, logger, setupRoutes);
        } else {
            logger.log(String.join("\n", validationErrors));
        }
    }

    private static List<String> runValidationAndReturnErrors(String[] args, String home) {
        List<Validator> validators = new ArrayList<Validator>(){{
            add(new PortValidator());
            add(new DirectoryValidator(home));
        }};

        return new ArgsValidation(validators).validate(args);
    }

    private static void setupAndRunServer(String[] args, String home, Logger logger, Function<Path, Routes> setupRoutes) {
        ArgsParser argsParser = new ArgsParser(args);
        Integer port = argsParser.parsePort();
        Path root = argsParser.parseRoot(home);

        Runtime runtime = Runtime.getRuntime();
        Routes routes = setupRoutes.apply(root);
        ServerRouter router = new ServerRouter(routes, root);
        RequestParser requestParser = new RequestParser();
        ResponseSerializer responseSerializer = new ResponseSerializer();
        Server server = new SetupServer(logger, runtime, router, requestParser, responseSerializer).setup(port);

        server.run();
    }
}