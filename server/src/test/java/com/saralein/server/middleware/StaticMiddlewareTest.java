package com.saralein.server.middleware;

import com.saralein.server.controller.Controller;
import com.saralein.server.filesystem.ServerPaths;
import com.saralein.server.mocks.MockController;
import com.saralein.server.protocol.Methods;
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
    private static final String DIRECTORY_RESPONSE = "Directory response";
    private static final String FILE_RESPONSE = "File response";
    private static final String PARTIAL_RESPONSE = "Partial response";
    private static final String ROUTER_RESPONSE = "Router response";
    private static final String HEADER_200 = "HTTP/1.1 200 OK\r\n\r\n";
    private static final String HEADER_405 = "HTTP/1.1 405 Method Not Allowed\r\nContent-Type: text/html\r\n\r\n";
    private static final String JPG_URI = "/cheetara.jpg";
    private static final String ROUTER_URI = "/router";

    private StaticMiddleware staticMiddleware;

    @Before
    public void setUp() {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        Controller directoryController = new MockController(200, DIRECTORY_RESPONSE);
        Controller fileController = new MockController(200, FILE_RESPONSE);
        Controller partialContentController = new MockController(200, PARTIAL_RESPONSE);
        Routes routes = new Routes().get(ROUTER_URI, new MockController(200, ROUTER_RESPONSE));
        Router router = new Router(routes);

        staticMiddleware = new StaticMiddleware(new ServerPaths(root), router, directoryController,
                fileController, partialContentController);
    }

    @Test
    public void returnsDirectoryResponseForDirectory() {
        Request request = new Request.Builder()
                .method(Methods.GET)
                .uri("/")
                .build();

        Response response = staticMiddleware.call(request);
        Header header = response.getHeader();

        assertEquals(HEADER_200, header.formatToString());
        assertArrayEquals(DIRECTORY_RESPONSE.getBytes(), response.getBody());
    }

    @Test
    public void returnsFileResponseForFile() {
        Request request = new Request.Builder()
                .method(Methods.GET)
                .uri(JPG_URI)
                .build();

        Response response = staticMiddleware.call(request);
        Header header = response.getHeader();

        assertEquals(HEADER_200, header.formatToString());
        assertArrayEquals(FILE_RESPONSE.getBytes(), response.getBody());
    }

    @Test
    public void returnsPartialResponseForFile() {
        Request request = new Request.Builder()
                .method(Methods.GET)
                .uri(JPG_URI)
                .addHeader("Range", "bytes=0-9")
                .build();

        Response response = staticMiddleware.call(request);
        Header header = response.getHeader();

        assertEquals(HEADER_200, header.formatToString());
        assertArrayEquals(PARTIAL_RESPONSE.getBytes(), response.getBody());
    }

    @Test
    public void returnsResponseFromRouterIfNotStaticResource() {
        Request request = new Request.Builder()
                .method(Methods.GET)
                .uri(ROUTER_URI)
                .build();
        Response response = staticMiddleware.call(request);

        assertArrayEquals(ROUTER_RESPONSE.getBytes(), response.getBody());
    }

    @Test
    public void returnsNotAllowedForIncorrectMethodOnResource() {
        Request request = new Request.Builder()
                .method(Methods.DELETE)
                .uri(JPG_URI)
                .build();
        Response response = staticMiddleware.call(request);
        Header header = response.getHeader();

        assertEquals(HEADER_405, header.formatToString());
    }
}