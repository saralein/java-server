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
        boolean dataExisted = dataStore.dataExistsForID(request.getUri());
        dataStore.deleteData(request.getUri());

        return new ResponseBuilder()
                   .addStatus(200)
                   .addHeader("Content-Type", "text/html")
                   .addBody(createBody(dataExisted))
                   .build();
    }

    private String createBody(Boolean dataExisted) {
        return dataExisted ? "Form data has been deleted." :
                             "No form data available for deletion.";
    }
}
