package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.FormStore;
import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

import java.util.HashMap;

public class FormGetController implements Controller {
    private final FormBody formBody;
    private FormStore formStore;

    public FormGetController(FormStore formStore, FormBody formBody) {
        this.formStore = formStore;
        this.formBody = formBody;
    }

    public Response respond(Request request) {
        return new Response.Builder()
                    .status(200)
                    .addHeader("Content-Type", "text/html")
                    .body(createBody(request))
                    .build();
    }

    private String createBody(Request request) {
        String uri = request.getUri();

        if (formStore.dataExistsForID(uri)) {
            HashMap<String, String> data = formStore.retrieveData(uri);
            return formBody.formatDataToHtml(data);
        } else {
            return "";
        }
    }
}