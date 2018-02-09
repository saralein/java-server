package com.saralein.cobspec.controller;

import com.saralein.cobspec.data.LogStore;
import com.saralein.server.authorization.Authorizer;
import com.saralein.server.controller.UnauthorizedController;
import com.saralein.server.exchange.Header;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.Base64;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LogControllerTest {
    private LogController logController;

    @Before
    public void setUp() {
        LogStore logStore = new LogStore();
        logStore.add("Message 1");
        logController = new LogController(
                logStore, new Authorizer("admin", "hunter2"),
                new UnauthorizedController("ServerCity"));
    }

    @Test
    public void returns401ForUnauthorizedRequest() {
        String auth = Base64.getEncoder().encodeToString("admin:password123".getBytes());
        Request request = new Request.Builder()
                .method("GET")
                .uri("/logs")
                .addHeader("Authorization", "Basic " + auth)
                .build();

        Response response = logController.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 401 Unauthorized\r\nWWW-Authenticate: " +
                "Basic realm=\"ServerCity\"\r\n\r\n", header.formatToString());
    }

    @Test
    public void returns401ForNoCredentialsRequest() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/logs")
                .build();

        Response response = logController.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 401 Unauthorized\r\nWWW-Authenticate: " +
                "Basic realm=\"ServerCity\"\r\n\r\n", header.formatToString());
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

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n", header.formatToString());
        assertTrue(new String(response.getBody()).contains("Message 1"));
    }
}
