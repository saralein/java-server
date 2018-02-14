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

public class PatchMiddlewareTest {
    private MockCallable mockCallable;
    private MockHandler mockHandler;
    private Middleware patchMiddleware;

    @Before
    public void setUp() {
        mockCallable = new MockCallable();
        mockHandler = new MockHandler(200, "Patch response");
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        FileHelper fileHelper = new FileHelper(root);
        patchMiddleware = new PatchMiddleware(fileHelper, mockHandler).apply(mockCallable);
    }

    @Test
    public void handlesFilePatchRequest() {
        Request request = new Request.Builder()
                .uri("/recipe.txt")
                .method("PATCH")
                .build();
        patchMiddleware.call(request);

        assert (mockHandler.wasCalled());
        assertFalse(mockCallable.wasCalled());
    }

    @Test
    public void passesNonPatchRequestToNextMiddleware() {
        Request request = new Request.Builder()
                .uri("/recipe.txt")
                .method("GET")
                .build();
        patchMiddleware.call(request);

        assert (mockCallable.wasCalled());
        assertFalse(mockHandler.wasCalled());
    }

    @Test
    public void passesNonFileRequestToNextMiddleware() {
        Request request = new Request.Builder()
                .uri("/")
                .method("GET")
                .build();
        patchMiddleware.call(request);

        assert (mockCallable.wasCalled());
        assertFalse(mockHandler.wasCalled());
    }
}
