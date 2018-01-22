package com.saralein.server;

import com.saralein.server.logger.Logger;
import com.saralein.server.middleware.*;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.router.Routes;
import com.saralein.server.router.Router;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Application {
    private final Caller middleware;

    public Application(Caller middleware) {
        this.middleware = middleware;
    }

    public Response call(Request request) {
        return middleware.call(request);
    }

    public static class Builder {
        private final Logger logger;
        private Path root;
        private Routes routes;
        private List<Middleware> middlewares;

        public Builder(Logger logger, Path root) {
            this.logger = logger;
            this.root = root;
            this.middlewares = new ArrayList<>();
        }

        public Builder router(Routes routes) {
            this.routes = routes;
            return this;
        }

        public Builder use(Middleware middleware) {
            middlewares.add(middleware);
            return this;
        }

        public Application build() {
            Caller middleware = applyMiddlewares();

            return new Application(middleware);
        }

        private Caller applyMiddlewares() {
            Router router = configureRouter();
            middlewares.add(new LoggingMiddleware(logger));
            Caller finalMiddleware = new StaticMiddleware(root, router);

            for (Middleware middleware : middlewares) {
                finalMiddleware = middleware.apply(finalMiddleware);
            }

            return finalMiddleware;
        }

        private Router configureRouter() {
            return this.routes == null
                    ? new Router(new Routes())
                    : new Router(routes);
        }
    }
}