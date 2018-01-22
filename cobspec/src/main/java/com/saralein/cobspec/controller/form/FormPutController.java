package com.saralein.cobspec.controller.form;

import com.saralein.server.controller.Controller;
import com.saralein.cobspec.data.DataStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.util.HashMap;

public class FormPutController implements Controller {
    private final DataStore dataStore;
    private final FormBody formBody;
    private final FormModification formModification;

    public FormPutController(DataStore dataStore, FormBody formBody, FormModification formModification) {
        this.dataStore = dataStore;
        this.formBody = formBody;
        this.formModification = formModification;
    }

    public Response respond(Request request) {
        String uri = request.getUri();
        HashMap<String, String> formData = dataStore.retrieveData(request.getUri());

        boolean succeeded = formModification.processFormData(request, formData, dataStore);
        Integer status = succeeded ? 200 : 400;
        String body = succeeded ? formBody.formatDataToHtml(dataStore.retrieveData(uri)) : "";

        return new Response.Builder()
                    .status(status)
                    .addHeader("Content-Type", "text/html")
                    .body(body)
                    .build();
    }
}
