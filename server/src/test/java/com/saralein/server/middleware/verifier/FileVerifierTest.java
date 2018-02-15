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

public class FileVerifierTest {
    private FileVerifier fileVerifier;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        fileVerifier = new FileVerifier(new File(sha1), new FilePath(root));
    }

    @Test
    public void verifiesFileRequest() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/recipe.txt")
                .build();
        assert (fileVerifier.validForMiddleware(request));
    }

    @Test
    public void invalidatesNonFileRequest() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/")
                .build();
        assertFalse(fileVerifier.validForMiddleware(request));
    }

    @Test
    public void invalidatesPartialRequest() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/")
                .addHeader("Range", "bytes=2-4")
                .build();
        assertFalse(fileVerifier.validForMiddleware(request));
    }

    @Test
    public void invalidatesNonGetOrHeadRequest() {
        Request request = new Request.Builder()
                .method("DELETE")
                .uri("/")
                .build();
        assertFalse(fileVerifier.validForMiddleware(request));
    }
}
