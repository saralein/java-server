package com.saralein.server.router;

import java.util.HashMap;
import java.util.Map;

public class RouteConfig {
    private final Map<String, String> config;

    public RouteConfig() {
        this.config = new HashMap<>();
    }

    public RouteConfig add(String key, String value) {
        config.put(key, value);
        return this;
    }

    public String getValue(String key) {
        return config.get(key);
    }
}
