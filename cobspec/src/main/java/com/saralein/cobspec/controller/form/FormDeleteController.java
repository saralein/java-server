package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.FormStore;
import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public class FormDeleteController implements Controller {
    private final FormStore formStore;

    public FormDeleteController(FormStore formStore) {
        this.formStore = formStore;
    }

    public Response respond(Request request) {
        formStore.deleteData(request.getUri());

        return new Response.Builder()
                    .status(200)
                    .addHeader("Content-Type", "text/html")
                    .body("Form data has been deleted.")
                    .build();
    }
}
