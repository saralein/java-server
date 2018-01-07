package com.saralein.server.middleware;

import com.saralein.server.controller.Controller;
import com.saralein.server.mocks.MockController;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import com.saralein.server.router.Router;
import com.saralein.server.router.Routes;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AuthMiddlewareTest {
    private Controller application;

    @Before
    public void setUp() {
        List<String> authRoutes = new ArrayList<String>(){{
            add("/logs");
        }};

        Routes routes = new Routes()
                .get("/logs", new MockController(200, "Hit logs"))
                .get("/stuff", new MockController(200, "Hits stuff"));
        Controller router = new Router(routes);

        AuthMiddleware authMiddleware = new AuthMiddleware("admin", "hunter2", "ServerCity", authRoutes);
        application = authMiddleware.use(router);
    }

    @Test
    public void requestWithCorrectAuthPassesToRouter() {
        String auth = Base64.getEncoder().encodeToString("admin:hunter2".getBytes());
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/logs");
            put("version", "HTTP/1.1");
            put("Authorization", "Basic " + auth);
        }});
        Response response = application.createResponse(request);

        assertArrayEquals("Hit logs".getBytes(), response.getBody());
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
        Response response = application.createResponse(request);
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
        Response response = application.createResponse(request);
        Header header = response.getHeader();

        String expected = "HTTP/1.1 401 Unauthorized\r\n" +
                          "WWW-Authenticate: Basic realm=\"ServerCity\"\r\n\r\n";

        assertEquals(expected, header.formatToString());
    }

    @Test
    public void requestForRouteWithNoAuthPassesToRouter() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/stuff");
            put("version", "HTTP/1.1");
        }});
        Response response = application.createResponse(request);

        assertArrayEquals("Hits stuff".getBytes(), response.getBody());
    }
}