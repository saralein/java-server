package com.saralein.cobspec.data;

import java.util.HashMap;

public interface DataStore {
    void addData(String id, HashMap<String, String> data);
    HashMap<String, String> retrieveData(String id);
    void deleteData(String id);
    boolean dataExistsForID(String id);
}