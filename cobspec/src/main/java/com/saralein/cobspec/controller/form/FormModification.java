package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.DataStore;
import com.saralein.core.request.Request;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FormModification {
    boolean processFormData(Request request, HashMap<String, String> data, DataStore dataStore) {
        if (containsValidData(request)) {
            HashMap<String, String> formData = serializeRequestBodyToData(request, data);
            dataStore.addData(request.getUri(), formData);
            return true;
        }

        return false;
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
}
