package com.saralein.cobspec.controller.form;

import com.saralein.server.controller.Controller;
import com.saralein.cobspec.data.DataStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class FormDeleteController implements Controller {
    private final DataStore dataStore;

    public FormDeleteController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Response respond(Request request) {
        dataStore.deleteData(request.getUri());

        return new Response.Builder()
                    .status(200)
                    .addHeader("Content-Type", "text/html")
                    .body("Form data has been deleted.")
                    .build();
    }
}
