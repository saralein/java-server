package com.saralein.server;

import com.saralein.server.mocks.MockController;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import com.saralein.server.router.Routes;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ApplicationTest {
    private Path root;
    private Request request;

    @Before
    public void setUp() {
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        request = new Request.Builder()
                .method("GET")
                .uri("/route")
                .build();
    }

    @Test
    public void callRunsApplicationStaticMiddleware() {
        Application application = new Application.Builder(root).build();
        Request request = new Request.Builder()
                .method("GET")
                .uri("/recipe.txt")
                .build();
        Response response = application.call(request);
        assertArrayEquals("1 cup rice".getBytes(), response.getBody());
    }

    @Test
    public void buildReturnsAnApplication() {
        Application.Builder applicationBuilder = new Application.Builder(root);
        assertEquals(Application.Builder.class, applicationBuilder.getClass());
    }

    @Test
    public void buildsRoutelessApplicationIfRoutesNotSet() {
        Application application = new Application.Builder(root).build();
        Response response = application.call(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
    }

    @Test
    public void buildsRoutedApplicationIfRoutesSet() {
        Routes routes = new Routes().get("/route", new MockController(200, "Reached route"));
        Application application = new Application.Builder(root)
                .router(routes)
                .build();
        Response response = application.call(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
        assertArrayEquals("Reached route".getBytes(), response.getBody());
    }
}