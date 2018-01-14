package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import java.util.Base64;
import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AuthControllerTest {
    private Controller authController;

    @Before
    public void setUp() {
        Controller controller = new DefaultController();
        authController = new AuthController("admin", "hunter2", "ServerCity", controller);
    }

    @Test
    public void requestWithCorrectAuthReturns200() {
        String auth = Base64.getEncoder().encodeToString("admin:hunter2".getBytes());
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/logs");
            put("version", "HTTP/1.1");
            put("Authorization", "Basic " + auth);
        }});
        Response response = authController.createResponse(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
    }

    @Test
    public void requestWithIncorrectAuthReturns401(){
        String auth = Base64.getEncoder().encodeToString("admin:password123".getBytes());
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/logs");
            put("version", "HTTP/1.1");
            put("Authorization", "Basic " + auth);
        }});
        Response response = authController.createResponse(request);
        Header header = response.getHeader();

        String expected = "HTTP/1.1 401 Unauthorized\r\n" +
                "WWW-Authenticate: Basic realm=\"ServerCity\"\r\n\r\n";

        assertEquals(expected, header.formatToString());
    }

    @Test
    public void requestWithNoAuthReturns401ForAuthRoute() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/logs");
            put("version", "HTTP/1.1");
        }});
        Response response = authController.createResponse(request);
        Header header = response.getHeader();

        String expected = "HTTP/1.1 401 Unauthorized\r\n" +
                "WWW-Authenticate: Basic realm=\"ServerCity\"\r\n\r\n";

        assertEquals(expected, header.formatToString());
    }
}