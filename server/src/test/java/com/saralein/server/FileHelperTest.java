package com.saralein.server;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileHelperTest {
    private FileHelper fileHelper;
    private List<String> fileNames;
    private Path root;
    private Path jpgPath;
    private Path txtPath;
    private Path gifPath;
    private Path subdirectory;

    @Before
    public void setUp() {
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        jpgPath = root.resolve("cheetara.jpg");
        txtPath = root.resolve("recipe.txt");
        gifPath = root.resolve("marshmallow.gif");
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
        assertEquals(jpgPath, fileHelper.createAbsolutePath("/cheetara.jpg"));
        assertEquals(txtPath, fileHelper.createAbsolutePath("/recipe.txt"));
        assertEquals(gifPath, fileHelper.createAbsolutePath("/marshmallow.gif"));
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
        assertEquals(10, fileHelper.getFileLength(txtPath));
        assertEquals(15587, fileHelper.getFileLength(jpgPath));
        assertEquals(2628712, fileHelper.getFileLength(gifPath));
    }

    @Test
    public void getsNamesOfFilesInDirectory() throws IOException {
        assertEquals(fileNames, fileHelper.listFileNames(root));
    }
}