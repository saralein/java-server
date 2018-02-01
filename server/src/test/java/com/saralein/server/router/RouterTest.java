package com.saralein.server.router;

import com.saralein.server.middleware.Middleware;
import com.saralein.server.mocks.MockCallable;
import com.saralein.server.mocks.MockController;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class RouterTest {
    private Middleware router;

    @Before
    public void setUp() {
        Routes routes = new Routes().get("/stuff", new MockController(200, "Some stuff!"));
        router = new Router(routes).apply(new MockCallable());
    }

    private Request createRequest(String method, String uri) {
        return new Request.Builder()
                .method(method)
                .uri(uri)
                .build();
    }

    @Test
    public void returns200ResponseForMatchingRoute() {
        Request request = createRequest("GET", "/stuff");
        Response response = router.call(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
        assertArrayEquals("Some stuff!".getBytes(), response.getBody());
    }

    @Test
    public void callsNextCallableIfResourceNotFound() {
        Request request = createRequest("GET", "/snarf.jpg");
        Response response = router.call(request);

        assertArrayEquals("Callable response".getBytes(), response.getBody());
    }

    @Test
    public void returnsNotAllowedForIncorrectMethodOnResource() {
        Request request = createRequest("DELETE", "/stuff");
        Response response = router.call(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 405 Method Not Allowed\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("405 Method Not Allowed".getBytes(), response.getBody());
    }
}