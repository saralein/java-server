package com.saralein.server.Controller.form;

import com.saralein.server.data.DataStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class FormPutController extends FormModification {
    public FormPutController(DataStore dataStore) {
        super(dataStore);
    }

    public Response createResponse(Request request) {
        return createResponseFromData(request, dataStore.retrieveData(request.getUri()));
    }
}
