package com.saralein.server.controller.form;

import com.saralein.server.Controller.form.FormPutController;
import com.saralein.server.data.FormStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import java.util.HashMap;
import java.util.LinkedHashMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FormPutControllerTest {
    private FormStore formStore;
    private FormPutController formPutController;

    @Before
    public void setUp() {
        formStore = new FormStore();

        formStore.addData("/form", new LinkedHashMap<String, String>(){{
            put("My", "Cat");
            put("More", "Stuff");
        }});

        formPutController = new FormPutController(formStore);
    }

    @Test
    public void updatesExistingDataForSingleData() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "PUT");
            put("uri", "/form");
            put("version", "HTTP/1.1");
            put("body", "My=Data");
        }});

        formPutController.createResponse(request);
        HashMap<String, String> data = formStore.retrieveData("/form");

        assertTrue(data.containsKey("My"));
        assertEquals("Data", data.get("My"));
        assertTrue(data.containsKey("More"));
        assertEquals("Stuff", data.get("More"));
    }

    @Test
    public void returnsCorrectResponseForSingleData() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "PUT");
            put("uri", "/form");
            put("version", "HTTP/1.1");
            put("body", "My=Data");
        }});

        Response response = formPutController.createResponse(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("<p>My=Data<br>More=Stuff<br></p>".getBytes(), response.getBody());
    }

    @Test
    public void updatesExistingDataForMultipleData() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "PUT");
            put("uri", "/form");
            put("version", "HTTP/1.1");
            put("body", "My=Data&More=Things");
        }});

        formPutController.createResponse(request);
        HashMap<String, String> data = formStore.retrieveData("/form");

        assertTrue(data.containsKey("My"));
        assertEquals("Data", data.get("My"));
        assertTrue(data.containsKey("More"));
        assertEquals("Things", data.get("More"));
    }

    @Test
    public void returnsCorrectResponseForMultipleData() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "PUT");
            put("uri", "/form");
            put("version", "HTTP/1.1");
            put("body", "My=Data&More=Things");
        }});

        Response response = formPutController.createResponse(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("<p>My=Data<br>More=Things<br></p>".getBytes(), response.getBody());
    }

    @Test
    public void returnsCorrectResponseForBadRequests() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "PUT");
            put("uri", "/form");
            put("version", "HTTP/1.1");
            put("body", "My=Data&MoreStuff");
        }});

        Response response = formPutController.createResponse(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 400 Bad Request\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("".getBytes(), response.getBody());
    }
}