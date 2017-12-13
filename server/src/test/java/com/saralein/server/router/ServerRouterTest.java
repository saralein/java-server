package com.saralein.server.router;

import com.saralein.server.controller.Controller;
import com.saralein.server.mocks.MockController;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ServerRouterTest {
    private ServerRouter router;

    @Before
    public void setUp() {
        String rootPath = System.getProperty("user.dir") + "/src/test/public";
        Path root = Paths.get(rootPath);

        Controller directoryController = new MockController(200, "Directory response");
        Controller fileController = new MockController(200, "File response");
        Controller notFoundController = new MockController(404, "Not found response");
        Routes routes = new Routes(new HashMap<>(), directoryController, fileController, notFoundController);

        router = new ServerRouter(routes, root);
    }

    @Test
    public void returnsNullResponseForNonExistentResources() {
        Request notFoundRequest = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/snarf.jpg");
            put("version", "HTTP/1.1");
        }});

        Response response = router.resolveRequest(notFoundRequest);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 404 Not Found\r\n\r\n", header.formatToString());
    }

    @Test
    public void returnsDirectoryResponseForDirectory() {
        Request directoryRequest = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/");
            put("version", "HTTP/1.1");
        }});

        Response response = router.resolveRequest(directoryRequest);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
        assertArrayEquals("Directory response".getBytes(), response.getBody());
    }

    @Test
    public void returnsFileResponseForFile() {
        Request fileRequest = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/cheetara.jpg");
            put("version", "HTTP/1.1");
        }});

        Response response = router.resolveRequest(fileRequest);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
        assertArrayEquals("File response".getBytes(), response.getBody());
    }
}