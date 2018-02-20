package com.saralein.server.middleware;

import com.saralein.server.exchange.Header;
import com.saralein.server.filesystem.File;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.mocks.MockCallable;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FileMethodMiddlewareTest {
    private MockCallable mockCallable;
    private Middleware fileMethodErrorMiddleware;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        mockCallable = new MockCallable();
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        fileMethodErrorMiddleware = new FileMethodMiddleware(new File(sha1), new FilePath(root)).apply(mockCallable);
    }

    @Test
    public void handlesFileRequestWithInvalidMethod() {
        Request request = new Request.Builder()
                .uri("/recipe.txt")
                .method("DELETE")
                .build();
        Response response = fileMethodErrorMiddleware.call(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 405 Method Not Allowed\r\nContent-Type: text/html\r\nAllow: GET,HEAD\r\n\r\n";

        assertEquals(expected, header.formatToString());
        assertFalse(mockCallable.wasCalled());
    }

    @Test
    public void passesFileRequestWithValidMethodToNextMiddleware() {
        Request request = new Request.Builder()
                .uri("/recipe.txt")
                .method("GET")
                .build();
        fileMethodErrorMiddleware.call(request);

        assert (mockCallable.wasCalled());
    }
}
