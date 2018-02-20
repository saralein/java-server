package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.FormStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;

public class FormPostControllerTest {
    private byte[] bodyArray;
    private Response response;
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
        response = formPostController.call(request);
    }

    @Test
    public void returnsResponseForValidRequests() {
        Response expected = new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/html")
                .body(bodyArray)
                .build();

        assert (response.equals(expected));
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
        Response expected = new Response.Builder()
                .status(400)
                .addHeader("Content-Type", "text/html")
                .build();
        Request request = new Request.Builder()
                .method("POST")
                .uri("/form")
                .body("My=Data&MoreStuff")
                .build();
        Response response = formPostController.call(request);

        assert (response.equals(expected));
    }
}
