package com.saralein.server;

import com.saralein.server.Controller.*;
import com.saralein.server.Controller.form.*;
import com.saralein.server.data.FormStore;
import com.saralein.server.logger.ConnectionLogger;
import com.saralein.server.logger.Logger;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.FileHelper;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.response.SysFileHelper;
import com.saralein.server.router.Routes;
import com.saralein.server.router.RoutesBuilder;
import com.saralein.server.router.ServerRouter;
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

        List<String> validationErrors = runValidationAndReturnErrors(args, home);

        if (validationErrors.isEmpty()) {
            setupAndRunServer(args, home, logger);
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

    private static void setupAndRunServer(String[] args, String home, Logger logger) {
        ArgsParser argsParser = new ArgsParser(args);
        Integer port = argsParser.parsePort();
        Path root = argsParser.parseRoot(home);

        Runtime runtime = Runtime.getRuntime();
        SysFileHelper sysFileHelper = new SysFileHelper(root);
        Routes routes = setupRoutes(sysFileHelper);
        ServerRouter router = new ServerRouter(routes, sysFileHelper);
        RequestParser requestParser = new RequestParser();
        ResponseSerializer responseSerializer = new ResponseSerializer();
        Server server = new SetupServer(logger, runtime, router, requestParser, responseSerializer).setup(port);

        server.run();
    }

    private static Routes setupRoutes(FileHelper fileHelper) {
        FormStore formStore = new FormStore();

        return new RoutesBuilder(new DirectoryController(fileHelper), new FileController(fileHelper), new NotFoundController())
                .addRoute("/redirect", "GET", new RedirectController())
                .addRoute("/form", "GET", new FormGetController(formStore))
                .addRoute("/form", "POST", new FormPostController(formStore))
                .addRoute("/form", "PUT", new FormPutController(formStore))
                .addRoute("/form", "DELETE", new FormDeleteController(formStore))
                .build();
    }
}
