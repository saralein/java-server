package com.saralein.server.middleware;

import com.saralein.server.authorization.Authorizer;
import com.saralein.server.exchange.Header;
import com.saralein.server.mocks.MockCallable;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.Base64;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AuthMiddlewareTest {
    private Middleware authMiddleware;
    private MockCallable mockCallable;
    private String unauthorized;

    @Before
    public void setUp() {
        mockCallable = new MockCallable();
        unauthorized = "HTTP/1.1 401 Unauthorized\r\nWWW-Authenticate: " +
                "Basic realm=\"ServerCity\"\r\nContent-Type: text/html\r\n\r\n";
        Authorizer authorizer = new Authorizer("admin", "hunter2");
        authMiddleware = new AuthMiddleware(
                authorizer, "/logs", "ServerCity").apply(mockCallable);
    }

    @Test
    public void returns401ForUnauthorizedRequest() {
        String auth = Base64.getEncoder().encodeToString("admin:password123".getBytes());
        Request request = new Request.Builder()
                .method("GET")
                .uri("/logs")
                .addHeader("Authorization", "Basic " + auth)
                .build();
        Response response = authMiddleware.call(request);
        Header header = response.getHeader();

        assertEquals(unauthorized, header.formatToString());
        assertFalse(mockCallable.wasCalled());
    }

    @Test
    public void returns401ForNoCredentialsRequest() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/logs")
                .build();

        Response response = authMiddleware.call(request);
        Header header = response.getHeader();

        assertEquals(unauthorized, header.formatToString());
        assertFalse(mockCallable.wasCalled());
    }

    @Test
    public void passesRequestWithCorrectAuthToNextMiddleware() {
        String auth = Base64.getEncoder().encodeToString("admin:hunter2".getBytes());
        Request request = new Request.Builder()
                .method("GET")
                .uri("/logs")
                .addHeader("Authorization", "Basic " + auth)
                .build();
        authMiddleware.call(request);

        assert (mockCallable.wasCalled());
    }

    @Test
    public void passesRequestWithNoRequiredAuthToNextMiddleware() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/coffee")
                .build();
        authMiddleware.call(request);

        assert (mockCallable.wasCalled());
    }
}
