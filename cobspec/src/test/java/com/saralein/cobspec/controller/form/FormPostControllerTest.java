package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.FormStore;
import com.saralein.server.exchange.Header;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class FormPostControllerTest {
    private byte[] bodyArray;
    private Response formResponse;
    private FormPostController formPostController;
    private FormStore formStore;

    @Before
    public void setUp() {
        String body = "<p>My=Data<br>More=Stuff<br></p>";
        bodyArray = body.getBytes();

        Request request = new Request.Builder()
                .method("POST")
                .uri("/form")
                .body("My=Data&More=Stuff")
                .build();

        formStore = new FormStore();
        FormBody formBody = new FormBody();
        FormModification formModification = new FormModification();
        formPostController = new FormPostController(formStore, formBody, formModification);
        formResponse = formPostController.respond(request);
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
        assertEquals("Data", data.get("My"));
        assertEquals("Stuff", data.get("More"));
    }

    @Test
    public void returnsCorrectResponseForBadRequests() {
        Request request = new Request.Builder()
                .method("POST")
                .uri("/form")
                .body("My=Data&MoreStuff")
                .build();

        Response response = formPostController.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 400 Bad Request\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("".getBytes(), response.getBody());
    }
}
