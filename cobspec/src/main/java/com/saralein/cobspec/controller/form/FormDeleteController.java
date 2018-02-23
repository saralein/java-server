package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.DataStore;
import com.saralein.server.callable.Callable;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class FormDeleteController implements Callable {
    private final DataStore dataStore;

    public FormDeleteController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Response call(Request request) {
        dataStore.deleteData(request.getUri());

        return new Response.Builder()
                    .status(200)
                    .addHeader("Content-Type", "text/html")
                    .body("Form data has been deleted.")
                    .build();
    }
}
