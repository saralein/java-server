package com.saralein.cobspec.controller;

import com.saralein.cobspec.data.LogStore;
import com.saralein.server.exchange.Header;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LogControllerTest {
    private LogController logController;

    @Before
    public void setUp() {
        LogStore logStore = new LogStore();
        logStore.add("Message 1");
        logStore.add("Message 2");
        logController = new LogController(logStore);
    }

    @Test
    public void returns200WithLogsInBody() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/logs")
                .build();
        Response response = logController.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n", header.formatToString());
        assertTrue(new String(response.getBody()).contains("Message 1"));
        assertTrue(new String(response.getBody()).contains("Message 2"));
    }
}
