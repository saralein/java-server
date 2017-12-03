package com.saralein.server.Controller.form;

import com.saralein.server.data.DataStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;
import java.util.HashMap;

public class FormGetController extends FormController {
    private DataStore dataStore;

    public FormGetController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Response createResponse(Request request) {
        return new ResponseBuilder()
                    .addStatus(200)
                    .addHeader("Content-Type", "text/html")
                    .addBody(createBody(request))
                    .build();
    }

    private String createBody(Request request) {
        String uri = request.getUri();

        if (dataStore.dataExistsForID(uri)) {
            HashMap<String, String> data = dataStore.retrieveData(uri);
            return formatDataToHtml(data);
        } else {
            return "";
        }
    }
}