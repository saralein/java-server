package com.saralein.server.filesystem;

import org.junit.Before;
import org.junit.Test;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static org.junit.Assert.assertEquals;

public class FileTest {
    private MessageDigest sha1;
    private File file;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        sha1 = MessageDigest.getInstance("SHA-1");
        file = new File(sha1);
    }

    @Test
    public void getsMimeTypesOfFiles() {
        assertEquals("application/pdf", file.mimeType("/cake.pdf"));
        assertEquals("image/jpeg", file.mimeType("/cheetara.jpg"));
        assertEquals("image/gif", file.mimeType("/marshmallow.gif"));
        assertEquals("text/plain", file.mimeType("/recipe.txt"));
    }

    @Test
    public void getsLengthOfFiles() throws IOException {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        Path resource = root.resolve("recipe.txt");

        assertEquals(10, file.length(resource));
    }

    @Test
    public void hashesFileContent() {
        byte[] content = "Hi".getBytes();;
        String hashed = DatatypeConverter.printHexBinary(sha1.digest(content)).toLowerCase();

        assertEquals(hashed, file.computeHash(content));
    }
}
