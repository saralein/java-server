package com.saralein.cobspec.controller.form;

import com.saralein.core.controller.Controller;
import com.saralein.cobspec.data.DataStore;
import com.saralein.core.request.Request;
import com.saralein.core.response.Response;
import com.saralein.core.response.ResponseBuilder;

public class FormDeleteController implements Controller {
    private final DataStore dataStore;

    public FormDeleteController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Response createResponse(Request request) {
        dataStore.deleteData(request.getUri());

        return new ResponseBuilder()
                    .addStatus(200)
                    .addHeader("Content-Type", "text/html")
                    .addBody("Form data has been deleted.")
                    .build();
    }
}
