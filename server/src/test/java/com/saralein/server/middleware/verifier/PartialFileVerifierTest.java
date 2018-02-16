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

public class PartialFileVerifierTest {
    private PartialFileVerifier partialFileVerifier;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        partialFileVerifier = new PartialFileVerifier(new File(sha1), new FilePath(root));
    }

    @Test
    public void verifiesPartialFileRequest() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/recipe.txt")
                .addHeader("Range", "bytes=2-4")
                .build();
        assert (partialFileVerifier.validForMiddleware(request));
    }

    @Test
    public void invalidatesNonPartialFileRequest() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/recipe.txt")
                .build();
        assertFalse(partialFileVerifier.validForMiddleware(request));
    }

    @Test
    public void invalidatesNonGetRequest() {
        Request request = new Request.Builder()
                .method("HEAD")
                .uri("/recipe.txt")
                .build();
        assertFalse(partialFileVerifier.validForMiddleware(request));
    }
}
