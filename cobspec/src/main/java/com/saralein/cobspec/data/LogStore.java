package com.saralein.cobspec.data;

import java.util.ArrayList;
import java.util.List;

public class LogStore {
    private final List<String> log;

    public LogStore() {
        this.log = new ArrayList<>();
    }

    public void add(String message) {
        log.add(message);
    }

    public String retrieveLog() {
        return String.join("", log);
    }

}
