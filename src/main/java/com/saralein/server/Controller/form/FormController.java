package com.saralein.server.Controller.form;

import com.saralein.server.Controller.Controller;
import com.saralein.server.data.DataStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class FormController implements Controller {
    private final DataStore dataStore;

    public FormController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Response createResponse(Request request) {
        HashMap<String, String> formData = serializeBodyToFormData(request);
        sendFormDataToStore(request, formData);

        return new ResponseBuilder()
                   .addStatus(200)
                   .addHeader("Content-Type", "text/html")
                   .addBody(createBody(formData))
                   .build();
    }

    private String createBody(HashMap<String, String> formData) {
        StringBuilder body = new StringBuilder();

        for (String key : formData.keySet()) {
            String html = "<h3>" + key + ": " + formData.get(key) + "</h3>";
            body.append(html);
        }

        return body.toString();
    }

    private HashMap<String, String> serializeBodyToFormData(Request request) {
        HashMap<String, String> formData = new LinkedHashMap<>();
        String[] splitBody = request.getBody().split("&");

        for (String dataPair : splitBody) {
            String[] dataSplit = dataPair.split("=");
            formData.put(dataSplit[0], dataSplit[1]);
        }

        return formData;
    }

    private void sendFormDataToStore(Request request, HashMap<String, String> formData) {
        dataStore.addData(request.getUri(), formData);
    }
}
