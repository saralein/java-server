package com.saralein.server;

import com.saralein.server.middleware.StaticMiddleware;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.router.Router;
import com.saralein.server.router.Routes;

import java.nio.file.Path;

public class Application {
    private final StaticMiddleware staticMiddleware;

    public Application(StaticMiddleware staticMiddleware) {
        this.staticMiddleware = staticMiddleware;
    }

    public Response call(Request request) {
        return staticMiddleware.call(request);
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
            Router router = configureRouter();
            StaticMiddleware staticMiddleware = new StaticMiddleware(root, router);
            return new Application(staticMiddleware);
        }

        private Router configureRouter() {
            return this.routes == null
                    ? new Router(new Routes())
                    : new Router(routes);
        }
    }
}
