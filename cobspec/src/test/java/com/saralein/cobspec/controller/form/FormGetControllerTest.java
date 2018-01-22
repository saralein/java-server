package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.DataStore;
import com.saralein.cobspec.data.FormStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import java.util.LinkedHashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        Response response = formGetController.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("".getBytes(), response.getBody());
    }

    @Test
    public void returnsCorrectResponseWhenStoreHasData() {
        dataStore.addData("/form", new LinkedHashMap<String, String>(){{
            put("My", "Data");
            put("More", "Stuff");
        }});
        Response response = formGetController.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("<p>My=Data<br>More=Stuff<br></p>".getBytes(), response.getBody());
    }
}
