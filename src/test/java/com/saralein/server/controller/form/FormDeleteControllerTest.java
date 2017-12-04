package com.saralein.server.controller.form;

import com.saralein.server.Controller.form.FormDeleteController;
import com.saralein.server.data.FormStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

public class FormDeleteControllerTest {
    private FormStore formStore;
    private Response formDeleteResponse;

    @Before
    public void setUp() {
        formStore = new FormStore();

        formStore.addData("/form", new HashMap<String, String>(){{
            put("My", "Data");
            put("Your", "Stuff");
        }});

        Request request = new Request(new HashMap<String, String>(){{
            put("method", "DELETE");
            put("uri", "/form");
            put("version", "HTTP/1.1");
        }});

        formDeleteResponse = new FormDeleteController(formStore).createResponse(request);
    }

    @Test
    public void deletesAllDataForURI() {
        assertTrue(formStore.retrieveData("/form").isEmpty());
    }

    @Test
    public void doesNotDeleteDataForOtherURI() {
        formStore.addData("/more", new HashMap<String, String>(){{
            put("More", "Things");
            put("Dill", "Pickles");
        }});

        assertTrue(formStore.retrieveData("/form").isEmpty());
        assertFalse(formStore.retrieveData("/more").isEmpty());
    }

    @Test
    public void returnsCorrectResponseIfDataExisted() {
        Header header = formDeleteResponse.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("Form data has been deleted.".getBytes(), formDeleteResponse.getBody());
    }

    @Test
    public void returnsCorrectResponseIfDataDidNotExist() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "DELETE");
            put("uri", "/pickles");
            put("version", "HTTP/1.1");
        }});

        Response datalessDeleteResponse = new FormDeleteController(formStore).createResponse(request);
        Header header = datalessDeleteResponse.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("Form data has been deleted.".getBytes(), datalessDeleteResponse.getBody());
    }
}