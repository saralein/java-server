package com.saralein.server.router;

import com.saralein.server.mocks.MockController;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RouterTest {
    private Router router;

    @Before
    public void setUp() {
        Routes routes = new Routes().get("/stuff", new MockController(200, "Some stuff!"));
        router = new Router(routes);
    }

    @Test
    public void returns200ResponseForMatchingRoute() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/stuff");
            put("version", "HTTP/1.1");
        }});

        Response response = router.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
        assertArrayEquals("Some stuff!".getBytes(), response.getBody());
    }

    @Test
    public void returnsNotFoundResponseForNonExistentResources() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/snarf.jpg");
            put("version", "HTTP/1.1");
        }});

        Response response = router.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("404: Page not found.".getBytes(), response.getBody());
    }

    @Test
    public void returnsNotAllowedForIncorrectMethodOnResource() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "DELETE");
            put("uri", "/stuff");
            put("version", "HTTP/1.1");
        }});

        Response response = router.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 405 Method Not Allowed\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("".getBytes(), response.getBody());
    }
}