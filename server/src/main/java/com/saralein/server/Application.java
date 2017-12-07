package com.saralein.server;

import com.saralein.server.controller.ErrorController;
import com.saralein.server.controller.DirectoryController;
import com.saralein.server.controller.FileController;
import com.saralein.server.logger.Logger;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.router.Routes;
import com.saralein.server.router.Router;

import java.nio.file.Path;

public class Application {
    private final Logger logger;
    private Routes routes;

    public Application(Logger logger) {
        this.logger = logger;
        this.routes = new Routes();
    }

    public Application config(Routes routes) {
        this.routes = routes;
        return this;
    }

    public void start(int port, Path root) {
        Runtime runtime = Runtime.getRuntime();
        FileHelper fileHelper = new FileHelper(root);

        DirectoryController directoryController = new DirectoryController(fileHelper);
        FileController fileController = new FileController(fileHelper);

        ErrorController errorController = new ErrorController();

        Router router = new Router(directoryController, fileController, errorController, routes, root);
        RequestParser requestParser = new RequestParser();
        ResponseSerializer responseSerializer = new ResponseSerializer();
        Server server = new ServerInitializer(logger, runtime, router, requestParser, responseSerializer).setup(port);

        server.run();
    }
}