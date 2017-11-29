package com.saralein.server.controller.form;

import com.saralein.server.Controller.form.FormController;
import com.saralein.server.data.FormStore;
import com.saralein.server.request.Request;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormControllerTest {
    private byte[] bodyArray;
    private FormController formController;
    private Response formResponse;
    private FormStore formStore;

    @Before
    public void setUp() {
        String body = "<h3>My: Data</h3><h3>More: Stuff</h3>";

        bodyArray = body.getBytes();

        RequestParser requestParser = new RequestParser();
        Request request = requestParser.parse("POST /form HTTP/1.1\r\n\r\nBody: My=Data&More=Stuff");

        formStore = new FormStore();
        formController = new FormController(formStore);
        formResponse = formController.createResponse(request);
    }

    @Test
    public void returnsResponseWithCorrectHeader() {
        Header header = formResponse.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
    }

    @Test
    public void returnsResponseWithCorrectBody() {
        assertArrayEquals(bodyArray, formResponse.getBody());
    }

    @Test
    public void addsCorrectDataToStore() {
        HashMap<String, String> data = formStore.retrieveData("/form");

        assertEquals(2, data.keySet().size());
        assertTrue(data.containsKey("My"));
        assertTrue(data.containsKey("More"));
        assertEquals("Data", data.get("My"));
        assertEquals("Stuff", data.get("More"));
    }
}