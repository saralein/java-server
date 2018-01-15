package com.saralein.server.middleware;

import com.saralein.server.mocks.MockMiddleware;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import java.util.Base64;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AuthMiddlewareTest {
    private Middleware authMiddleware;

    @Before
    public void setUp() {
        MockMiddleware mockMiddleware = new MockMiddleware();
        authMiddleware = new AuthMiddleware("admin", "hunter2", "ServerCity");
        authMiddleware.apply(mockMiddleware);
    }

    @Test
    public void authorizedRequestPassesToNextMiddleware() {
        String auth = Base64.getEncoder().encodeToString("admin:hunter2".getBytes());
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/logs");
            put("version", "HTTP/1.1");
            put("Authorization", "Basic " + auth);
        }});
        Response response = authMiddleware.call(request);

        assertArrayEquals("Middleware response".getBytes(), response.getBody());
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
        Response response = authMiddleware.call(request);
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
        Response response = authMiddleware.call(request);
        Header header = response.getHeader();

        String expected = "HTTP/1.1 401 Unauthorized\r\n" +
                "WWW-Authenticate: Basic realm=\"ServerCity\"\r\n\r\n";

        assertEquals(expected, header.formatToString());
    }
}