package com.saralein.server.middleware;

import com.saralein.server.callable.Callable;

public interface Middleware extends Callable {
    Middleware apply(Callable callable);
}
