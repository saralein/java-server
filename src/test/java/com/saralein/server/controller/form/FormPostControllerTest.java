package com.saralein.server.controller.form;

import com.saralein.server.Controller.form.FormPostController;
import com.saralein.server.data.FormStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormPostControllerTest {
    private byte[] bodyArray;
    private Response formResponse;
    private FormPostController formPostController;
    private FormStore formStore;

    @Before
    public void setUp() {
        String body = "<p>My=Data<br>More=Stuff<br></p>";

        bodyArray = body.getBytes();

        Request request = new Request(new HashMap<String, String>(){{
            put("method", "POST");
            put("uri", "/form");
            put("version", "HTTP/1.1");
            put("body", "My=Data&More=Stuff");
        }});

        formStore = new FormStore();
        formPostController = new FormPostController(formStore);
        formResponse = formPostController.createResponse(request);
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

    @Test
    public void returnsCorrectResponseForBadRequests() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "POST");
            put("uri", "/form");
            put("version", "HTTP/1.1");
            put("body", "My=Data&MoreStuff");
        }});

        Response response = formPostController.createResponse(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 400 Bad Request\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("".getBytes(), response.getBody());
    }
}