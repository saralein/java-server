package com.saralein.server.middleware;

import com.saralein.server.controller.Controller;

public interface Middleware {
    Controller use(Controller controller);
}
