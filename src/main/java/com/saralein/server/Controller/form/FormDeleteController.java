package com.saralein.server.Controller.form;

import com.saralein.server.Controller.Controller;
import com.saralein.server.data.DataStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;

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
