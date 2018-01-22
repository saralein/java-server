package com.saralein.server.filesystem;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ServerPathsTest {
    private Path jpgPath;
    private Path txtPath;
    private Path gifPath;
    private Path subdirectory;
    private Path root;
    private ServerPaths serverPaths;

    @Before
    public void setUp() {
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        jpgPath = root.resolve("cheetara.jpg");
        txtPath = root.resolve("recipe.txt");
        gifPath = root.resolve("marshmallow.gif");
        subdirectory = root.resolve("sloths");
        serverPaths = new ServerPaths(root);
    }

    @Test
    public void getsAbsolutePathOfFile() {
        assertEquals(jpgPath, serverPaths.createAbsolutePath("/cheetara.jpg"));
        assertEquals(txtPath, serverPaths.createAbsolutePath("/recipe.txt"));
        assertEquals(gifPath, serverPaths.createAbsolutePath("/marshmallow.gif"));
    }

    @Test
    public void getsDifferenceFromRootPath() {
        assertEquals("/recipe.txt", serverPaths.createRelativePathString("recipe.txt", root));
        assertEquals("/sloths/sleepy.gif", serverPaths.createRelativePathString("sleepy.gif", subdirectory));
        assertEquals("/sloths/space", serverPaths.createRelativePathString("space", subdirectory));
    }
}
