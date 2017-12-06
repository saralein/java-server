package com.saralein.server.Controller.form;

import java.util.HashMap;

public class FormBody {
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