package com.saralein.server.middleware;

import com.saralein.server.FileHelper;
import com.saralein.server.controller.Controller;
import com.saralein.server.mocks.MockController;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import com.saralein.server.router.Router;
import com.saralein.server.router.Routes;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class StaticMiddlewareTest {
    private final String uri;
    private final String expected;
    private StaticMiddleware staticMiddleware;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"/", "Directory response"},
                {"/cheetara.jpg", "File response"},
                {"/router", "Router response" }
        });
    }

    @Before
    public void setUp() {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        Controller directoryController = new MockController(200, "Directory response");
        Controller fileController = new MockController(200, "File response");
        Routes routes = new Routes().get("/router", new MockController(200, "Router response"));
        Router router = new Router(routes);

        staticMiddleware = new StaticMiddleware(new FileHelper(root), router, directoryController, fileController);
    }

    public StaticMiddlewareTest(String uri, String expected) {
        this.uri = uri;
        this.expected = expected;
    }


    @Test
    public void returnsProperResponseBasedOnType() {
        Request request = new Request.Builder()
                .method("GET")
                .uri(uri)
                .build();
        Response response = staticMiddleware.call(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
        assertArrayEquals(expected.getBytes(), response.getBody());
    }
}