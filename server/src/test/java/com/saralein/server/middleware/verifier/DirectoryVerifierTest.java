package com.saralein.server.middleware.verifier;

import com.saralein.server.filesystem.Directory;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.request.Request;
import org.junit.Before;
import org.junit.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.assertFalse;

public class DirectoryVerifierTest {
    private DirectoryVerifier directoryVerifier;

    @Before
    public void setUp() {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        directoryVerifier = new DirectoryVerifier(new Directory(), new FilePath(root));
    }

    @Test
    public void verifiesDirectoryRequest() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/")
                .build();
        assert (directoryVerifier.validForMiddleware(request));
    }

    @Test
    public void invalidatesNonDirectoryRequest() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/recipe.txt")
                .build();
        assertFalse(directoryVerifier.validForMiddleware(request));
    }
}
