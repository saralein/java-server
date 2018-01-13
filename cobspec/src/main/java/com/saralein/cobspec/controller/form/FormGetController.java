package com.saralein.cobspec.controller.form;

import com.saralein.server.controller.Controller;
import com.saralein.cobspec.data.DataStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;
import java.util.HashMap;

public class FormGetController implements Controller {
    private final FormBody formBody;
    private DataStore dataStore;

    public FormGetController(DataStore dataStore, FormBody formBody) {
        this.dataStore = dataStore;
        this.formBody = formBody;
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
            return formBody.formatDataToHtml(data);
        } else {
            return "";
        }
    }
}