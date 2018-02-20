package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.DataStore;
import com.saralein.cobspec.data.FormStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.LinkedHashMap;

public class FormGetControllerTest {
    private Request request;
    private FormGetController formGetController;
    private DataStore dataStore;

    @Before
    public void setUp() {
        request = new Request.Builder()
                .method("GET")
                .uri("/form")
                .build();

        dataStore = new FormStore();
        FormBody formBody = new FormBody();
        formGetController = new FormGetController(dataStore, formBody);
    }

    @Test
    public void returnsCorrectResponseWhenStoreIsEmpty() {
        Response expected = new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/html")
                .build();
        Response response = formGetController.call(request);

        assert (response.equals(expected));
    }

    @Test
    public void returnsCorrectResponseWhenStoreHasData() {
        Response expected = new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/html")
                .body("<p>My=Data<br>More=Stuff<br></p>")
                .build();
        dataStore.addData("/form", new LinkedHashMap<String, String>(){{
            put("My", "Data");
            put("More", "Stuff");
        }});
        Response response = formGetController.call(request);


        assert (response.equals(expected));
    }
}
