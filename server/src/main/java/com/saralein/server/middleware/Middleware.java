package com.saralein.server.middleware;

public interface Middleware extends Callable {
    Middleware apply(Callable callable);
}
