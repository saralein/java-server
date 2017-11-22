package com.saralein.server.response;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SysFileHelperTest {
    private Path root;
    private SysFileHelper fileHelper;
    private List<String> fileNames;
    private String separator = FileSystems.getDefault().getSeparator();
    private String rootPath = System.getProperty("user.dir") + "/" + "public";
    private String jpgPath = rootPath + separator + "cheetara.jpg";
    private String txtPath = rootPath + separator + "recipe.txt";
    private String gifPath = rootPath + separator + "marshmallow.gif";
    private Path subdirectory = Paths.get(rootPath + separator + "sloths");

    @Before
    public void setUp() {
        root = Paths.get(rootPath);
        fileHelper = new SysFileHelper(root);
        fileNames = new ArrayList<>();

        fileNames.add("cake.pdf");
        fileNames.add("cheetara.jpg");
        fileNames.add("marshmallow.gif");
        fileNames.add("recipe.txt");
        fileNames.add("sloths");
    }

    @Test
    public void getsAbsolutePathOfFile() {
        assertEquals(jpgPath, fileHelper.createAbsolutePath("cheetara.jpg"));
        assertEquals(txtPath, fileHelper.createAbsolutePath("recipe.txt"));
        assertEquals(gifPath, fileHelper.createAbsolutePath("marshmallow.gif"));
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
    public void getsNamesOfFilesInDirectory() {
        try {
            assertEquals(fileNames, fileHelper.listFileNames(root));
        } catch (IOException e) {
            fail("Failed to list file names in File Helper test.");
        }
    }
}