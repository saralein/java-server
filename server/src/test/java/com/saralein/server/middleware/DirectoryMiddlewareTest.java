package com.saralein.server.middleware;

import com.saralein.server.FileHelper;
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
        FileHelper fileHelper = new FileHelper(root);
        directoryMiddleware = new DirectoryMiddleware(fileHelper, mockHandler).apply(mockCallable);
    }

    @Test
    public void callsHandlerForDirectoryRequests() {
        Request request = new Request.Builder()
                .uri("/")
                .method("GET")
                .build();
        directoryMiddleware.call(request);

        assert (mockHandler.wasCalled());
        assertFalse(mockCallable.wasCalled());
    }

    @Test
    public void callsCallableForOtherRequests() {
        Request request = new Request.Builder()
                .uri("/recipe.txt")
                .method("GET")
                .build();
        directoryMiddleware.call(request);

        assert (mockCallable.wasCalled());
        assertFalse(mockHandler.wasCalled());
    }
}
