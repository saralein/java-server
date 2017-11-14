package com.saralein.server.response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SysFileHelperTest {
    private SysFileHelper fileHelper;
    private String userDir;
    private File directory;
    private List<String> filenames;
    private String jpgPath = "public/cheetara.jpg";
    private String txtPath = "public/recipe.txt";
    private String gifPath = "public/marshmallow.gif";

    @Before
    public void setUp() {
        userDir = System.getProperty("user.dir") + "/";
        fileHelper = new SysFileHelper("public");
        directory = new File("public");
        filenames = new ArrayList<>();

        filenames.add("cake.pdf");
        filenames.add("cheetara.jpg");
        filenames.add("marshmallow.gif");
        filenames.add("recipe.txt");
    }

    @Test
    public void getsRelativePathOfFile() {
        assertEquals(jpgPath, fileHelper.createRelativePath("cheetara.jpg"));
        assertEquals(txtPath, fileHelper.createRelativePath("recipe.txt"));
        assertEquals(gifPath, fileHelper.createRelativePath("marshmallow.gif"));
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
        assertEquals(filenames, fileHelper.listFileNames(directory));
    }
}