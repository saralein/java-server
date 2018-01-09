package com.saralein.server;

import com.saralein.server.controller.Controller;
import com.saralein.server.logger.Logger;
import com.saralein.server.middleware.Middleware;
import com.saralein.server.middleware.StaticMiddleware;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.router.Routes;
import com.saralein.server.router.Router;
import java.nio.file.Path;

public class Application {
    private final Logger logger;
    private Routes routes;
    private Router router;
    private Path root = null;

    public Application(Logger logger) {
        this.logger = logger;
        this.routes = new Routes();
        this.router = new Router(routes);
    }

    public Application router(Routes routes) {
        this.routes = routes;
        this.router = new Router(routes);
        return this;
    }

    public Application addStatic(Path root) {
        this.root = root;
        return this;
    }

    public void start(int port) {
        Runtime runtime = Runtime.getRuntime();
        Controller application = configureStaticMiddleware();
        RequestParser requestParser = new RequestParser();
        ResponseSerializer responseSerializer = new ResponseSerializer();
        Server server = new ServerInitializer(logger, runtime, application, requestParser, responseSerializer).setup(port);
        server.run();
    }

    private Controller configureStaticMiddleware() {
        Middleware staticMiddleware = new StaticMiddleware(root);
        return root != null ? staticMiddleware.use(router) : router;
    }
}