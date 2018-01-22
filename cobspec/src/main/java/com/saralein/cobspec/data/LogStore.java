package com.saralein.cobspec.data;

public interface LogStore {
    void add(String message);
    String retrieveLog();
}
