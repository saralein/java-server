package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.DataStore;
import com.saralein.server.callable.Callable;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.util.HashMap;

public class FormGetController implements Callable {
    private final FormBody formBody;
    private DataStore dataStore;

    public FormGetController(DataStore dataStore, FormBody formBody) {
        this.dataStore = dataStore;
        this.formBody = formBody;
    }

    public Response call(Request request) {
        return new Response.Builder()
                    .status(200)
                    .addHeader("Content-Type", "text/html")
                    .body(createBody(request))
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
