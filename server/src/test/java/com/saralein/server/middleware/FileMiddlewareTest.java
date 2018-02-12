package com.saralein.server.middleware;

import com.saralein.server.filesystem.File;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.mocks.MockCallable;
import com.saralein.server.mocks.MockHandler;
import com.saralein.server.request.Request;
import org.junit.Before;
import org.junit.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static org.junit.Assert.assertFalse;

public class FileMiddlewareTest {
    private MockHandler mockHandler;
    private MockCallable mockCallable;
    private Middleware fileMiddleware;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        mockHandler = new MockHandler(200, "File response");
        mockCallable = new MockCallable();
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        fileMiddleware = new FileMiddleware(new File(sha1), new FilePath(root), mockHandler).apply(mockCallable);
    }

    @Test
    public void handlesFileRequest() {
        Request request = new Request.Builder()
                .uri("/recipe.txt")
                .method("GET")
                .build();
        fileMiddleware.call(request);

        assert (mockHandler.wasCalled());
        assertFalse(mockCallable.wasCalled());
    }

    @Test
    public void passesNonFileRequestToNextMiddleware() {
        Request request = new Request.Builder()
                .uri("/")
                .method("GET")
                .build();
        fileMiddleware.call(request);

        assert (mockCallable.wasCalled());
        assertFalse(mockHandler.wasCalled());
    }
}
