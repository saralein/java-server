package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.FormStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import static org.junit.Assert.*;

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
        formStore.addData("/more", new HashMap<String, String>(){{
            put("More", "Things");
            put("Dill", "Pickles");
        }});

        Request request = new Request.Builder()
                .method("DELETE")
                .uri("/form")
                .build();

        formDeleteResponse = new FormDeleteController(formStore).respond(request);
    }

    @Test
    public void deletesAllDataForURI() {
        assertTrue(formStore.retrieveData("/form").isEmpty());
    }

    @Test
    public void doesNotDeleteDataForOtherURI() {
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
        Request request = new Request.Builder()
                .method("DELETE")
                .uri("/pickles")
                .build();
        Response response = new FormDeleteController(formStore).respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("Form data has been deleted.".getBytes(), response.getBody());
    }
}
