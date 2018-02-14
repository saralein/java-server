package com.saralein.server.middleware;

import com.saralein.server.filesystem.Directory;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.mocks.MockCallable;
import com.saralein.server.mocks.MockHandler;
import com.saralein.server.request.Request;
import org.junit.Before;
import org.junit.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.assertFalse;

public class DirectoryMiddlewareTest {
    private MockCallable mockCallable;
    private MockHandler mockHandler;
    private Middleware directoryMiddleware;

    @Before
    public void setUp() {
        mockCallable = new MockCallable();
        mockHandler = new MockHandler(200, "Patch response");
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        directoryMiddleware = new DirectoryMiddleware(
                new Directory(), new FilePath(root), mockHandler).apply(mockCallable);
    }

    @Test
    public void handlesDirectoryRequest() {
        Request request = new Request.Builder()
                .uri("/")
                .method("GET")
                .build();
        directoryMiddleware.call(request);

        assert (mockHandler.wasCalled());
        assertFalse(mockCallable.wasCalled());
    }

    @Test
    public void passesNonDirectoryRequestToNextMiddleware() {
        Request request = new Request.Builder()
                .uri("/recipe.txt")
                .method("GET")
                .build();
        directoryMiddleware.call(request);

        assert (mockCallable.wasCalled());
        assertFalse(mockHandler.wasCalled());
    }
}
