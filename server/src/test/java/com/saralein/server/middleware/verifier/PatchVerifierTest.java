package com.saralein.server.middleware.verifier;

import com.saralein.server.filesystem.File;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.request.Request;
import org.junit.Before;
import org.junit.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static org.junit.Assert.assertFalse;

public class PatchVerifierTest {
    private PatchVerifier patchVerifier;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        patchVerifier = new PatchVerifier(new File(sha1), new FilePath(root));
    }

    @Test
    public void verifiesPatchRequest() {
        Request request = new Request.Builder()
                .method("PATCH")
                .uri("/recipe.txt")
                .build();
        assert (patchVerifier.validForMiddleware(request));
    }

    @Test
    public void invalidatesNonPatchRequest() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/recipe.txt")
                .build();
        assertFalse(patchVerifier.validForMiddleware(request));
    }

    @Test
    public void invalidatesNonFileRequest() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/")
                .build();
        assertFalse(patchVerifier.validForMiddleware(request));
    }
}
