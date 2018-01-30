package com.saralein.server;

import com.saralein.server.controller.DirectoryController;
import com.saralein.server.controller.ErrorController;
import com.saralein.server.controller.FileController;
import com.saralein.server.controller.PartialContentController;
import com.saralein.server.filesystem.ServerFileIO;
import com.saralein.server.logger.Logger;
import com.saralein.server.partial_content.RangeParser;
import com.saralein.server.partial_content.RangeValidator;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.router.Router;
import com.saralein.server.router.Routes;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        ErrorController errorController = new ErrorController();
        DirectoryController directoryController = new DirectoryController(fileHelper);
        PartialContentController partialContentController = new PartialContentController(
                fileHelper, new ServerFileIO(), new RangeValidator(), new RangeParser(), errorController);
        FileController fileController = new FileController(fileHelper, partialContentController);

        Router router = new Router(directoryController, fileController, errorController, routes, root);
        RequestParser requestParser = new RequestParser();
        ResponseSerializer responseSerializer = new ResponseSerializer();
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        Server server = new ServerInitializer(
                logger, runtime, router, requestParser, responseSerializer, threadPool).setup(port);

        server.run();
    }
}