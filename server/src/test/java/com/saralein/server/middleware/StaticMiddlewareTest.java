package com.saralein.server.middleware;

import com.saralein.server.FileHelper;
import com.saralein.server.mocks.MockCallable;
import com.saralein.server.mocks.MockHandler;
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
    private static final String OK_HEADER = "HTTP/1.1 200 OK\r\n\r\n";
    private final String uri;
    private final String expectedBody;
    private final String expectedHeader;
    private final String method;
    private StaticMiddleware staticMiddleware;

    public StaticMiddlewareTest(String method, String uri, String expectedBody, String expectedHeader) {
        this.method = method;
        this.uri = uri;
        this.expectedBody = expectedBody;
        this.expectedHeader = expectedHeader;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"GET", "/", "Directory response", OK_HEADER},
                {"GET", "/cheetara.jpg", "File response", OK_HEADER},
                {"GET", "/router", "Callable response", OK_HEADER},
                {"DELETE", "/cheetara.jpg", "405 Method Not Allowed",
                        "HTTP/1.1 405 Method Not Allowed\r\nContent-Type: text/html\r\n\r\n"}
        });
    }

    @Before
    public void setUp() {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        MockHandler directoryHandler = new MockHandler(200, "Directory response");
        MockHandler fileHandler = new MockHandler(200, "File response");
        MockCallable mockCallable = new MockCallable();

        staticMiddleware = new StaticMiddleware(new FileHelper(root), directoryHandler, fileHandler);
        staticMiddleware.apply(mockCallable);
    }

    @Test
    public void returnsProperResponseBasedOnType() {
        Request request = new Request.Builder()
                .method(method)
                .uri(uri)
                .build();
        Response response = staticMiddleware.call(request);
        Header header = response.getHeader();

        assertEquals(expectedHeader, header.formatToString());
        assertArrayEquals(expectedBody.getBytes(), response.getBody());
    }
}