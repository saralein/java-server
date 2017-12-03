package com.saralein.server.Controller.form;

import com.saralein.server.data.DataStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

abstract class FormModification extends FormController {
    final DataStore dataStore;

    FormModification(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    Response createResponseFromData(Request request, HashMap<String, String> data) {
        if (containsValidData(request)) {
            HashMap<String, String> formData = serializeRequestBodyToData(request, data);
            sendFormDataToStore(request, formData);
            return buildResponse(200, createBody(formData));
        }

        return buildResponse(400, "");
    }

    private Response buildResponse(Integer status, String body) {
        return new ResponseBuilder()
                    .addStatus(status)
                    .addHeader("Content-Type", "text/html")
                    .addBody(body)
                    .build();
    }

    private String createBody(HashMap<String, String> formData) {
        return formatDataToHtml(formData);
    }

    private HashMap<String, String> serializeRequestBodyToData(
            Request request, HashMap<String, String> formData) {
        splitRequestBody(request).stream()
                                 .map(dataPair -> dataPair.split("=", 2))
                                 .forEach(dataPair -> formData.put(dataPair[0], dataPair[1]));

        return formData;
    }

    private List<String> splitRequestBody(Request request) {
        return Arrays.asList(request.getBody().split("&"));
    }

    private boolean containsValidData(Request request) {
        return splitRequestBody(request).stream()
                                        .map(data -> data.contains("="))
                                        .reduce(true, (result, containsEqual) -> result && containsEqual);
    }

    private void sendFormDataToStore(Request request, HashMap<String, String> formData) {
        dataStore.addData(request.getUri(), formData);
    }
}
