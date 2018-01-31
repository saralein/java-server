package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.FormStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class FormGetControllerTest {
    private Request request;
    private FormGetController formGetController;
    private FormStore formStore;

    @Before
    public void setUp() {
        request = new Request.Builder()
                .method("GET")
                .uri("/form")
                .build();

        formStore = new FormStore();
        FormBody formBody = new FormBody();
        formGetController = new FormGetController(formStore, formBody);
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
        formStore.addData("/form", new LinkedHashMap<String, String>() {{
            put("My", "Data");
            put("More", "Stuff");
        }});
        Response response = formGetController.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("<p>My=Data<br>More=Stuff<br></p>".getBytes(), response.getBody());
    }
}
