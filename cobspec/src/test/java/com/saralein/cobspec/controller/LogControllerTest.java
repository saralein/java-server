package com.saralein.cobspec.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import static org.junit.Assert.*;

public class LogControllerTest {
    private LogController logController;

    @Before
    public void setUp() {
        Path logTxt = Paths.get(System.getProperty("user.dir") + "/src/test/controller-test.txt");
        logController = new LogController(logTxt);
    }

    @Test
    public void returns200ForAuthorizedRequest() {
        String auth = Base64.getEncoder().encodeToString("admin:hunter2".getBytes());
        Request request = new Request.Builder()
                .method("GET")
                .uri("/logs")
                .addHeader("Authorization", "Basic " + auth)
                .build();
        Response response = logController.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
        assertTrue(new String(response.getBody()).contains("GET /logs HTTP/1.1"));
    }
}