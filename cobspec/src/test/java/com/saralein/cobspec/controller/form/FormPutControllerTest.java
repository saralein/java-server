package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.FormStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.LinkedHashMap;
import static org.junit.Assert.assertEquals;

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

    private Request createRequest(String body) {
        return new Request.Builder()
                .method("PUT")
                .uri("/form")
                .body(body)
                .build();
    }

    @Test
    public void updatesExistingDataForSingleData() {
        Request request = createRequest("My=Data");
        formPutController.call(request);
        HashMap<String, String> data = formStore.retrieveData("/form");

        assertEquals(2, data.keySet().size());
        assertEquals("Data", data.get("My"));
        assertEquals("Stuff", data.get("More"));
    }

    @Test
    public void returnsCorrectResponseForSingleData() {
        Response expected = new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/html")
                .body("<p>My=Data<br>More=Stuff<br></p>")
                .build();
        Request request = createRequest("My=Data");
        Response response = formPutController.call(request);

        assert (response.equals(expected));
    }

    @Test
    public void updatesExistingDataForMultipleData() {
        Request request = createRequest("My=Data&More=Things");
        formPutController.call(request);
        HashMap<String, String> data = formStore.retrieveData("/form");

        assertEquals(2, data.keySet().size());
        assertEquals("Data", data.get("My"));
        assertEquals("Things", data.get("More"));
    }

    @Test
    public void returnsCorrectResponseForMultipleData() {
        Response expected = new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/html")
                .body("<p>My=Data<br>More=Things<br></p>")
                .build();
        Request request = createRequest("My=Data&More=Things");
        Response response = formPutController.call(request);

        assert (response.equals(expected));
    }

    @Test
    public void returnsCorrectResponseForBadRequests() {
        Response expected = new Response.Builder()
                .status(400)
                .addHeader("Content-Type", "text/html")
                .build();
        Request request = createRequest("My=Data&MoreStuff");
        Response response = formPutController.call(request);


        assert (response.equals(expected));
    }
}
