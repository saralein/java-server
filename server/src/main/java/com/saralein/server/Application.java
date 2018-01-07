package com.saralein.server;

import com.saralein.server.controller.Controller;
import com.saralein.server.controller.DirectoryController;
import com.saralein.server.controller.FileController;
import com.saralein.server.logger.Logger;
import com.saralein.server.middleware.Middleware;
import com.saralein.server.middleware.StaticMiddleware;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.router.Routes;
import com.saralein.server.router.Router;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Application {
    private final Logger logger;
    private Routes routes;
    private Router router;
    private List<Middleware> middlewareList = new ArrayList<>();
    private Path root = null;

    public Application(Logger logger) {
        this.logger = logger;
        this.routes = new Routes();
        this.router = new Router(routes);
    }

    public Application config(Routes routes) {
        this.routes = routes;
        return this;
    }

    public Application addStatic(Path root) {
        this.root = root;
        return this;
    }

    public Application router(Routes routes) {
        this.routes = routes;
        this.router = new Router(routes);
        return this;
    }

    public Application use(Middleware middleware) {
        middlewareList.add(middleware);
        return this;
    }

    public void start(int port) {
        Runtime runtime = Runtime.getRuntime();
        Controller application = configureMiddleware();
        RequestParser requestParser = new RequestParser();
        ResponseSerializer responseSerializer = new ResponseSerializer();
        Server server = new ServerInitializer(logger,runtime, application, requestParser, responseSerializer).setup(port);

        server.run();
    }

    private Controller configureMiddleware() {
        return addClientMiddleware(addStaticMiddlewareIfApplicable(router));
    }

    private Controller addStaticMiddlewareIfApplicable(Controller router) {
        if (this.root != null) {
            FileHelper fileHelper = new FileHelper(root);

            DirectoryController directoryController = new DirectoryController(fileHelper);
            FileController fileController = new FileController(fileHelper);
            Middleware staticMiddleware = new StaticMiddleware(root, directoryController, fileController);
            return staticMiddleware.use(router);
        } else {
            return router;
        }
    }

    private Controller addClientMiddleware(Controller application) {
        Controller modifiedApplication = application;

        for(int i = middlewareList.size() - 1; i >= 0; i--) {
            Middleware middleware = middlewareList.get(i);
            modifiedApplication = middleware.use(modifiedApplication);
        }

        return modifiedApplication;
    }
}