package com.saralein.server.middleware;

import com.saralein.server.filesystem.File;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.mocks.MockCallable;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static org.junit.Assert.assertFalse;

public class FileMethodMiddlewareTest {
    private MockCallable mockCallable;
    private Middleware fileMethodErrorMiddleware;

    private Request requestWithMethod(String method) {
        return new Request.Builder()
                .uri("/recipe.txt")
                .method(method)
                .build();
    }

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        mockCallable = new MockCallable();
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        fileMethodErrorMiddleware = new FileMethodMiddleware(new File(sha1), new FilePath(root)).apply(mockCallable);
    }

    @Test
    public void handlesFileRequestWithInvalidMethod() {
        Response expected = new ErrorResponse(405).respond("Allow", "GET,HEAD");
        Request request = requestWithMethod("DELETE");
        Response response = fileMethodErrorMiddleware.call(request);
        assert (response.equals(expected));
        assertFalse(mockCallable.wasCalled());
    }

    @Test
    public void passesFileRequestWithValidMethodToNextMiddleware() {
        Request request = requestWithMethod("GET");
        fileMethodErrorMiddleware.call(request);
        assert (mockCallable.wasCalled());
    }
}
