package com.saralein.server.filesystem;

import org.junit.Before;
import org.junit.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.assertEquals;

public class FilePathTest {
    private Path root;
    private Path subdirectory;
    private FilePath filePath;

    @Before
    public void setUp() {
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        subdirectory = root.resolve("sloths");
        filePath = new FilePath(root);
    }

    @Test
    public void getsAbsolutePathOfFile() {
        assertEquals(root.resolve("cheetara.jpg"), filePath.absolute("/cheetara.jpg"));
        assertEquals(root.resolve("recipe.txt"), filePath.absolute("/recipe.txt"));
        assertEquals(root.resolve("marshmallow.gif"), filePath.absolute("/marshmallow.gif"));
    }

    @Test
    public void getsDifferenceFromRootPath() {
        assertEquals("/sloths/sleepy.gif", filePath.relative("sleepy.gif", subdirectory));
        assertEquals("/sloths/space", filePath.relative("space", subdirectory));
    }
}
