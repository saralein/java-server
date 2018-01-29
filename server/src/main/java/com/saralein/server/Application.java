package com.saralein.server;

import com.saralein.server.middleware.Callable;
import com.saralein.server.middleware.Middleware;
import com.saralein.server.middleware.StaticMiddleware;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.router.Router;
import com.saralein.server.router.Routes;

import java.nio.file.Path;

public class Application {
    private final Callable callable;

    public Application(Callable callable) {
        this.callable = callable;
    }

    public Response call(Request request) {
        return callable.call(request);
    }

    public static class Builder {
        private Path root;
        private Routes routes;

        public Builder(Path root) {
            this.root = root;
        }

        public Builder router(Routes routes) {
            this.routes = routes;
            return this;
        }

        public Application build() {
            Callable router = configureRouter();
            Middleware staticMiddleware = new StaticMiddleware(root);

            return new Application(staticMiddleware.apply(router));
        }

        private Router configureRouter() {
            return this.routes == null
                    ? new Router(new Routes())
                    : new Router(routes);
        }
    }
}
