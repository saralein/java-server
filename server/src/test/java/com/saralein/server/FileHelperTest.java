package com.saralein.server;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class FileHelperTest {
    private Path root;
    private Path subdirectory;
    private FileHelper fileHelper;
    private List<String> fileNames;

    @Before
    public void setUp() {
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        subdirectory = root.resolve("sloths");
        fileHelper = new FileHelper(root);
        fileNames = new ArrayList<>();

        fileNames.add("cake.pdf");
        fileNames.add("cheetara.jpg");
        fileNames.add("marshmallow.gif");
        fileNames.add("recipe.txt");
        fileNames.add("sloths/");
    }

    @Test
    public void getsAbsolutePathOfFile() {
        assertEquals(root.resolve("cheetara.jpg"), fileHelper.createAbsolutePath("/cheetara.jpg"));
        assertEquals(root.resolve("recipe.txt"), fileHelper.createAbsolutePath("/recipe.txt"));
        assertEquals(root.resolve("marshmallow.gif"), fileHelper.createAbsolutePath("/marshmallow.gif"));
    }

    @Test
    public void getsDifferenceFromRootPath() {
        assertEquals("/sloths/sleepy.gif", fileHelper.createRelativeFilePath("sleepy.gif", subdirectory));
        assertEquals("/sloths/space", fileHelper.createRelativeFilePath("space", subdirectory));
    }

    @Test
    public void getsMimeTypesOfFiles() {
        assertEquals("application/pdf", fileHelper.determineMimeType("/cake.pdf"));
        assertEquals("image/jpeg", fileHelper.determineMimeType("/cheetara.jpg"));
        assertEquals("image/gif", fileHelper.determineMimeType("/marshmallow.gif"));
        assertEquals("text/plain", fileHelper.determineMimeType("/recipe.txt"));
    }

    @Test
    public void getsLengthOfFiles() throws IOException {
        int txtLength = 10;
        assertEquals(txtLength, fileHelper.getFileLength(root.resolve("recipe.txt")));
    }

    @Test
    public void getsNamesOfFilesInDirectory() throws IOException {
        assertEquals(fileNames, fileHelper.listFileNames(root));
    }
}
