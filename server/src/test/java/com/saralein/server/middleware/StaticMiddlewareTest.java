package com.saralein.server.middleware;

import com.saralein.server.FileHelper;
import com.saralein.server.exchange.Header;
import com.saralein.server.mocks.MockCallable;
import com.saralein.server.mocks.MockHandler;
import com.saralein.server.request.Request;
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
    private final Request request;
    private final String expectedBody;
    private final String expectedHeader;
    private StaticMiddleware staticMiddleware;

    public StaticMiddlewareTest(Request request, String expectedBody, String expectedHeader) {
        this.request = request;
        this.expectedBody = expectedBody;
        this.expectedHeader = expectedHeader;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Request partialRequest = new Request.Builder()
                .method("GET")
                .uri("/recipe.txt")
                .addHeader("Range", "bytes=0-9")
                .build();

        return Arrays.asList(new Object[][] {
                {getRequest("GET", "/"), "Directory response", OK_HEADER},
                {getRequest("GET", "/cheetara.jpg"), "File response", OK_HEADER},
                {getRequest("GET", "/router"), "Callable response", OK_HEADER},
                {getRequest("DELETE", "/cheetara.jpg"), "405 Method Not Allowed",
                        "HTTP/1.1 405 Method Not Allowed\r\nContent-Type: text/html\r\n\r\n"},
                {partialRequest, "Partial file response", OK_HEADER}
        });
    }

    private static Request getRequest(String method, String uri) {
        return new Request.Builder()
                .method(method)
                .uri(uri)
                .build();
    }

    @Before
    public void setUp() {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        MockHandler directoryHandler = new MockHandler(200, "Directory response");
        MockHandler fileHandler = new MockHandler(200, "File response");
        MockHandler partialFileHandler = new MockHandler(200, "Partial file response");
        MockCallable mockCallable = new MockCallable();

        staticMiddleware = new StaticMiddleware(
                new FileHelper(root), directoryHandler, fileHandler, partialFileHandler);
        staticMiddleware.apply(mockCallable);
    }

    @Test
    public void returnsProperResponseBasedOnType() {
        Response response = staticMiddleware.call(request);
        Header header = response.getHeader();

        assertEquals(expectedHeader, header.formatToString());
        assertArrayEquals(expectedBody.getBytes(), response.getBody());
    }
}
