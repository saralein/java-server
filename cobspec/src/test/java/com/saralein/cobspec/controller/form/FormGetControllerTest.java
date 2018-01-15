package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.DataStore;
import com.saralein.cobspec.data.FormStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FormGetControllerTest {
    private byte[] formWithBodyArray;
    private Response formResponseWithBody;
    private Response formResponseWithoutBody;

    @Before
    public void setUp() {
        String formWithBody = "<p>My=Data<br>More=Stuff<br></p>";
        formWithBodyArray = formWithBody.getBytes();

        Request requestWithBody = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/form");
            put("version", "HTTP/1.1");
        }});

        Request requestWithoutBody = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/form");
            put("version", "HTTP/1.1");
        }});

        DataStore dataStore = new FormStore();
        FormBody formBody = new FormBody();
        FormGetController formGetController = new FormGetController(dataStore, formBody);

        formResponseWithoutBody = formGetController.respond(requestWithoutBody);

        dataStore.addData("/form", new LinkedHashMap<String, String>(){{
            put("My", "Data");
            put("More", "Stuff");
        }});

        formResponseWithBody = formGetController.respond(requestWithBody);
    }

    @Test
    public void returnsResponseWithCorrectHeader() {
        Header header = formResponseWithBody.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
    }

    @Test
    public void returnsResponseWithCorrectBody() {
        assertArrayEquals("".getBytes(), formResponseWithoutBody.getBody());
        assertArrayEquals(formWithBodyArray, formResponseWithBody.getBody());
    }
}