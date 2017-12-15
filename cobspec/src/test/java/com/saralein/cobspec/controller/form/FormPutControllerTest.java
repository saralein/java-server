package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.FormStore;
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

        FormBody formBody = new FormBody();
        FormModification formModification = new FormModification();
        formPutController = new FormPutController(formStore, formBody, formModification);
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

        assertEquals(2, data.keySet().size());
        assertEquals("Data", data.get("My"));
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

        assertEquals(2, data.keySet().size());
        assertEquals("Data", data.get("My"));
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