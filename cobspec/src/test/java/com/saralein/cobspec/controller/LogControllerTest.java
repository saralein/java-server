package com.saralein.cobspec.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;

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
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/logs");
            put("version", "HTTP/1.1");
            put("Authorization", "Basic " + auth);
        }});

        Response response = logController.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
        assertTrue(new String(response.getBody()).contains("GET /logs HTTP/1.1"));
    }
}