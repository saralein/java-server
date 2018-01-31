package com.saralein.server.router;

import com.saralein.server.controller.Controller;
import com.saralein.server.controller.ErrorController;
import com.saralein.server.mocks.MockController;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class RouterTest {
    private Router router;

    @Before
    public void setUp() {
        String rootPath = System.getProperty("user.dir") + "/src/test/public";
        Path root = Paths.get(rootPath);

        Controller directoryController = new MockController(200, "Directory response");
        Controller fileController = new MockController(200, "File response");
        ErrorController errorController = new ErrorController();
        Routes routes = new Routes()
                .get("/parameters", new MockController(200, "Parameter response"));

        router = new Router(directoryController, fileController, errorController, routes, root);
    }

    @Test
    public void returnsNotFoundResponseForNonExistentResources() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/snarf.jpg")
                .build();
        Response response = router.resolveRequest(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
    }

    @Test
    public void returnsDirectoryResponseForDirectory() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/")
                .build();

        Response response = router.resolveRequest(request);
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

        Response response = router.resolveRequest(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
        assertArrayEquals("File response".getBytes(), response.getBody());
    }

    @Test
    public void returnsNotAllowedForIncorrectMethodOnResource() {
        Request request = new Request.Builder()
                .method("DELETE")
                .uri("/")
                .build();

        Response response = router.resolveRequest(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 405 Method Not Allowed\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
    }
}
