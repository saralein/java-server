package com.saralein.cobspec.controller.form;

import com.saralein.cobspec.data.FormStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FormDeleteControllerTest {
    private FormStore formStore;
    private Response response;
    private Response expected;

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

        response = new FormDeleteController(formStore).call(request);
        expected = new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/html")
                .body("Form data has been deleted.")
                .build();
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
        assert (response.equals(expected));
    }

    @Test
    public void returnsCorrectResponseIfDataDidNotExist() {
        Request request = new Request.Builder()
                .method("DELETE")
                .uri("/pickles")
                .build();
        Response response = new FormDeleteController(formStore).call(request);

        assert (response.equals(expected));
    }
}
