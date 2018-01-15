package com.saralein.server.middleware;

public abstract class Middleware implements Caller {
    protected Caller middleware;

    public final Middleware apply(Caller caller) {
        this.middleware = caller;
        return this;
    }
}
