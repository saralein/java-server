package com.saralein.server.middleware;

import com.saralein.server.FileHelper;
import com.saralein.server.controller.Controller;
import com.saralein.server.mocks.MockCallable;
import com.saralein.server.mocks.MockController;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class StaticMiddlewareTest {
    private final String uri;
    private final String expected;
    private StaticMiddleware staticMiddleware;

    public StaticMiddlewareTest(String uri, String expected) {
        this.uri = uri;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"/", "Directory response"},
                {"/cheetara.jpg", "File response"},
                {"/router", "Callable response"}
        });
    }

    @Before
    public void setUp() {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        Controller directoryController = new MockController(200, "Directory response");
        Controller fileController = new MockController(200, "File response");
        MockCallable mockCallable = new MockCallable();

        staticMiddleware = new StaticMiddleware(new FileHelper(root), directoryController, fileController);
        staticMiddleware.apply(mockCallable);
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