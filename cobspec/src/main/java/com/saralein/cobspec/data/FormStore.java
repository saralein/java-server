package com.saralein.cobspec.data;

import java.util.HashMap;

public class FormStore implements DataStore {
   private HashMap<String, HashMap<String, String>> dataStore;

   public FormStore() {
       this.dataStore = new HashMap<>();
   }

   public void addData(String id, HashMap<String, String> data) {
       dataStore.put(id, data);
   }

   public HashMap<String, String> retrieveData(String id) {
       return dataStore.getOrDefault(id, new HashMap<>());
   }

   public void deleteData(String id) {
       dataStore.remove(id);
   }

   public boolean dataExistsForID(String id) {
       return dataStore.containsKey(id);
   }
}
