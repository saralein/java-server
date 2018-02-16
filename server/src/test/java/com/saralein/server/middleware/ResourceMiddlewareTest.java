package com.saralein.server.middleware;

import com.saralein.server.filesystem.File;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.middleware.verifier.FileVerifier;
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

public class ResourceMiddlewareTest {
    private MockHandler mockHandler;
    private MockCallable mockCallable;
    private Middleware fileMiddleware;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        mockHandler = new MockHandler(200, "File response");
        mockCallable = new MockCallable();
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        File file = new File(sha1);
        FilePath filePath = new FilePath(root);
        fileMiddleware = new ResourceMiddleware(mockHandler, new FileVerifier(file, filePath)).apply(mockCallable);
    }

    @Test
    public void handlesResourceRequest() {
        Request request = new Request.Builder()
                .uri("/recipe.txt")
                .method("GET")
                .build();
        fileMiddleware.call(request);

        assert (mockHandler.wasCalled());
        assertFalse(mockCallable.wasCalled());
    }

    @Test
    public void passesNonMatchingRequestToNextMiddleware() {
        Request request = new Request.Builder()
                .uri("/")
                .method("GET")
                .build();
        fileMiddleware.call(request);

        assert (mockCallable.wasCalled());
        assertFalse(mockHandler.wasCalled());
    }
}
