package com.saralein.server.response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SysFileHelperTest {
    private File root;
    private SysFileHelper fileHelper;
    private List<String> fileNames;
    private String rootPath = System.getProperty("user.dir") + "/" + "public";
    private String jpgPath = rootPath + File.separator + "cheetara.jpg";
    private String txtPath = rootPath + File.separator + "recipe.txt";
    private String gifPath = rootPath + File.separator + "marshmallow.gif";
    private File subdirectory = new File(rootPath + File.separator + "sloths");

    @Before
    public void setUp() {
        root = new File(rootPath);
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
        assertEquals("sloths/sleepy.gif", fileHelper.createRelativeFilePath("sleepy.gif", subdirectory));
        assertEquals("sloths/space", fileHelper.createRelativeFilePath("space", subdirectory));
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
        assertEquals(fileNames, fileHelper.listFileNames(root));
    }
}