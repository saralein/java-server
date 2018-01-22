package com.saralein.server.middleware;

import com.saralein.server.mocks.MockController;
import com.saralein.server.mocks.MockMiddleware;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import com.saralein.server.router.RouteConfig;
import com.saralein.server.router.Routes;
import java.util.Base64;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AuthMiddlewareTest {
    private Middleware authMiddleware;

    @Before
    public void setUp() {
        RouteConfig routeConfig = new RouteConfig()
                .add("username", "admin")
                .add("password", "hunter2");
        Routes routes = new Routes()
                .get("/logs", new MockController(200, "Log response"))
                .use("/logs", routeConfig);
        MockMiddleware mockMiddleware = new MockMiddleware();
        authMiddleware = new AuthMiddleware(routes, "ServerCity");
        authMiddleware.apply(mockMiddleware);
    }

    @Test
    public void authorizedRequestPassesToNextMiddleware() {
        String auth = Base64.getEncoder().encodeToString("admin:hunter2".getBytes());
        Request request = new Request.Builder()
                .method("GET")
                .uri("/logs")
                .addHeader("Authorization", "Basic " + auth)
                .build();
        Response response = authMiddleware.call(request);

        assertArrayEquals("Middleware response".getBytes(), response.getBody());
    }

    @Test
    public void requestWithIncorrectAuthReturns401(){
        String auth = Base64.getEncoder().encodeToString("admin:password123".getBytes());
        Request request = new Request.Builder()
                .method("GET")
                .uri("/logs")
                .addHeader("Authorization", "Basic " + auth)
                .build();
        Response response = authMiddleware.call(request);
        Header header = response.getHeader();

        String expected = "HTTP/1.1 401 Unauthorized\r\n" +
                "WWW-Authenticate: Basic realm=\"ServerCity\"\r\n\r\n";

        assertEquals(expected, header.formatToString());
    }

    @Test
    public void requestWithNoAuthReturns401ForAuthRoute() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/logs")
                .build();
        Response response = authMiddleware.call(request);
        Header header = response.getHeader();

        String expected = "HTTP/1.1 401 Unauthorized\r\n" +
                "WWW-Authenticate: Basic realm=\"ServerCity\"\r\n\r\n";

        assertEquals(expected, header.formatToString());
    }
}