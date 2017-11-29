package com.saralein.server.controller.form;

import com.saralein.server.Controller.form.FormGetController;
import com.saralein.server.data.DataStore;
import com.saralein.server.data.FormStore;
import com.saralein.server.request.Request;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import java.util.LinkedHashMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FormGetControllerTest {
    byte[] formWithBodyArray;
    DataStore dataStore;
    FormGetController formGetController;
    Response formResponseWithBody;
    Response formResponseWithoutBody;

    @Before
    public void setUp() {
        String formWithBody = "<p>My=Data<br>More=Stuff<br></p>";
        formWithBodyArray = formWithBody.getBytes();

        Request requestWithBody = new RequestParser().parse("GET /form HTTP/1.1\r\n\r\nBody: My=Data&More=Stuff");
        Request requestWithoutBody = new RequestParser().parse("GET /form HTTP/1.1\r\n");

        dataStore = new FormStore();
        formGetController = new FormGetController(dataStore);

        formResponseWithoutBody = formGetController.createResponse(requestWithoutBody);

        dataStore.addData("/form", new LinkedHashMap<String, String>(){{
            put("My", "Data");
            put("More", "Stuff");
        }});

        formResponseWithBody = formGetController.createResponse(requestWithBody);
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