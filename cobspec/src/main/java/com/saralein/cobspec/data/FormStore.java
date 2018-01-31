package com.saralein.cobspec.data;

import java.util.HashMap;

public class FormStore {
    private HashMap<String, HashMap<String, String>> formStore;

   public FormStore() {
       this.formStore = new HashMap<>();
   }

   public void addData(String id, HashMap<String, String> data) {
       formStore.put(id, data);
   }

   public HashMap<String, String> retrieveData(String id) {
       return formStore.getOrDefault(id, new HashMap<>());
   }

   public void deleteData(String id) {
       formStore.remove(id);
   }

   public boolean dataExistsForID(String id) {
       return formStore.containsKey(id);
   }
}
