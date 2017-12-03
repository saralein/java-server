package com.saralein.server.Controller.form;

import com.saralein.server.Controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.util.HashMap;

abstract class FormController implements Controller {
    public abstract Response createResponse(Request request);

    String formatDataToHtml(HashMap<String, String> data) {
        StringBuilder dataHtml = new StringBuilder();

        dataHtml.append("<p>");

        data.keySet().stream()
                     .map(key -> key + "=" + data.get(key) + "<br>")
                     .forEach(dataHtml::append);

        dataHtml.append("</p>");

        return dataHtml.toString();
    }
}
