package com.saralein.server.Controller.form;

import com.saralein.server.Controller.Controller;
import com.saralein.server.data.DataStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;
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

    public Response createResponse(Request request) {
        String uri = request.getUri();
        HashMap<String, String> formData = dataStore.retrieveData(request.getUri());

        boolean succeeded = formModification.processFormData(request, formData, dataStore);
        Integer status = succeeded ? 200 : 400;
        String body = succeeded ? formBody.formatDataToHtml(dataStore.retrieveData(uri)) : "";

        return new ResponseBuilder()
                    .addStatus(status)
                    .addHeader("Content-Type", "text/html")
                    .addBody(body)
                    .build();
    }
}
