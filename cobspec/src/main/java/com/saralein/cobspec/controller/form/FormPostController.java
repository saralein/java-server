package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.DataStore;
import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class FormPostController implements Controller {
    private final DataStore dataStore;
    private final FormBody formBody;
    private final FormModification formModification;

    public FormPostController(DataStore dataStore, FormBody formBody, FormModification formModification) {
        this.dataStore = dataStore;
        this.formBody = formBody;
        this.formModification = formModification;
    }

    public Response call(Request request) {
        HashMap<String, String> formData = new LinkedHashMap<>();

        boolean succeeded = formModification.processFormData(request, formData, dataStore);
        Integer status = succeeded ? 200 : 400;
        String body = succeeded ? formBody.formatDataToHtml(dataStore.retrieveData(request.getUri())) : "";

        return new Response.Builder()
                    .status(status)
                    .addHeader("Content-Type", "text/html")
                    .body(body)
                    .build();
    }
}
