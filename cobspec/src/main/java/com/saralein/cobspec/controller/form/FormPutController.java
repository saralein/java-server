package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.FormStore;
import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

import java.util.HashMap;

public class FormPutController implements Controller {
    private final FormStore formStore;
    private final FormBody formBody;
    private final FormModification formModification;

    public FormPutController(FormStore formStore, FormBody formBody, FormModification formModification) {
        this.formStore = formStore;
        this.formBody = formBody;
        this.formModification = formModification;
    }

    public Response respond(Request request) {
        String uri = request.getUri();
        HashMap<String, String> formData = formStore.retrieveData(request.getUri());

        boolean succeeded = formModification.processFormData(request, formData, formStore);
        Integer status = succeeded ? 200 : 400;
        String body = succeeded ? formBody.formatDataToHtml(formStore.retrieveData(uri)) : "";

        return new Response.Builder()
                    .status(status)
                    .addHeader("Content-Type", "text/html")
                    .body(body)
                    .build();
    }
}
