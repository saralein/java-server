package com.saralein.server.middleware;

import com.saralein.server.controller.Controller;
import com.saralein.server.mocks.MockController;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import com.saralein.server.router.Router;
import com.saralein.server.router.Routes;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StaticMiddlewareTest {
    private StaticMiddleware staticMiddleware;

    @Before
    public void setUp() {
        String rootPath = System.getProperty("user.dir") + "/src/test/public";
        Path root = Paths.get(rootPath);
        Controller directoryController = new MockController(200, "Directory response");
        Controller fileController = new MockController(200, "File response");
        Routes routes = new Routes().get("/router", new MockController(200, "Router response"));
        Router router = new Router(routes);

        staticMiddleware = new StaticMiddleware(root, router, directoryController, fileController);
    }

    @Test
    public void returnsDirectoryResponseForDirectory() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/")
                .build();

        Response response = staticMiddleware.call(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
        assertArrayEquals("Directory response".getBytes(), response.getBody());
    }

    @Test
    public void returnsFileResponseForFile() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/cheetara.jpg")
                .build();

        Response response = staticMiddleware.call(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
        assertArrayEquals("File response".getBytes(), response.getBody());
    }

    @Test
    public void returnsResponseFromRouterIfNotStaticResource() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/router")
                .build();
        Response response = staticMiddleware.call(request);

        assertArrayEquals("Router response".getBytes(), response.getBody());
    }
}