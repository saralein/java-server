package com.saralein.cobspec.data;

import java.util.ArrayList;
import java.util.List;

public class Log implements LogStore {
    List<String> log;

    public Log() {
        this.log = new ArrayList<>();
    }

    public void add(String message) {
        log.add(message);
    }

    public String retrieveLog() {
        return String.join("", log);
    }

}
