package com.saralein.server.Controller.form;

import com.saralein.server.data.DataStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.util.LinkedHashMap;

public class FormPostController extends FormModification {
    public FormPostController(DataStore dataStore) {
        super(dataStore);
    }

    public Response createResponse(Request request) {
        return createResponseFromData(request, new LinkedHashMap<>());
    }
}
