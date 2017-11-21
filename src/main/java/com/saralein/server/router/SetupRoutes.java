package com.saralein.server.router;

import com.saralein.server.Controller.RedirectController;

public class SetupRoutes {
    public Routes setup() {
        Routes routes = new Routes();

        routes.addRoute("/redirect", new RedirectController());

        return routes;
    }
}
