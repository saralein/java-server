package com.saralein.cobspec.controller;

import com.saralein.cobspec.data.LogStore;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.Base64;


public class LogControllerTest {
    private LogController logController;

    @Before
    public void setUp() {
        LogStore logStore = new LogStore();
        logStore.add("Message 1");
        logController = new LogController(logStore);
    }

    @Test
    public void returns200ForAuthorizedRequest() {
        Response expected = new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/plain")
                .body("Message 1")
                .build();
        String auth = Base64.getEncoder().encodeToString("admin:hunter2".getBytes());
        Request request = new Request.Builder()
                .method("GET")
                .uri("/logs")
                .addHeader("Authorization", "Basic " + auth)
                .build();
        Response response = logController.call(request);

        assert (response.equals(expected));
    }
}
