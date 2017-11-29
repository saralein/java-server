package com.saralein.server.data;

import java.util.HashMap;

public interface DataStore {
    void addData(String id, HashMap<String, String> data);
    HashMap<String, String> retrieveData(String id);
    boolean dataExistsForID(String id);
}