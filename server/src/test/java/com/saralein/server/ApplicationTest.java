package com.saralein.server;

import com.saralein.server.mocks.MockController;
import com.saralein.server.mocks.MockLogger;
import com.saralein.server.mocks.MockMiddleware;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import com.saralein.server.router.Routes;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ApplicationTest {
    private Path root;
    private MockLogger logger;
    private MockMiddleware middleware;
    private Request request;

    @Before
    public void setUp() {
        String rootPath = System.getProperty("user.dir") + "/src/test/public";
        root = Paths.get(rootPath);
        logger = new MockLogger();
        middleware = new MockMiddleware();
        request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/route");
        }});
    }

    @Test
    public void callRunsApplicationMiddleware() {
        Application application = new Application(middleware);
        Request request = new Request(new HashMap<>());
        Response response = application.call(request);
        assertArrayEquals("Middleware response".getBytes(), response.getBody());
    }

    @Test
    public void buildReturnsAnApplication() {
        Application.Builder applicationBuilder = new Application.Builder(logger, root);
        assertEquals(Application.Builder.class, applicationBuilder.getClass());
    }

    @Test
    public void buildsRoutelessApplicationIfRoutesNotSet() {
        Application application = new Application.Builder(logger, root).build();
        Response response = application.call(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
    }

    @Test
    public void buildsRoutedApplicationIfRoutesSet() {
        Routes routes = new Routes().get("/route", new MockController(200, "Reached route"));
        Application application = new Application.Builder(logger, root)
                .router(routes)
                .build();
        Response response = application.call(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
        assertArrayEquals("Reached route".getBytes(), response.getBody());
    }

    @Test
    public void usesApplicationMiddlewareIfSet() {
        Application application = new Application.Builder(logger, root)
                .use(new MockMiddleware())
                .build();
        Response response  = application.call(request);

        assertArrayEquals("Middleware response".getBytes(), response.getBody());
    }
}