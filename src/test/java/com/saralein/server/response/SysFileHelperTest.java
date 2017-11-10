package com.saralein.server.response;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
        assertEquals(jpgPath, fileHelper.getRelativePath("cheetara.jpg"));
        assertEquals(txtPath, fileHelper.getRelativePath("recipe.txt"));
        assertEquals(gifPath, fileHelper.getRelativePath("marshmallow.gif"));
    }

    @Test
    public void getsMimeTypesOfFiles() {
        assertEquals("application/pdf", fileHelper.getMimeType("/cake.pdf"));
        assertEquals("image/jpeg", fileHelper.getMimeType("/cheetara.jpg"));
        assertEquals("image/gif", fileHelper.getMimeType("/marshmallow.gif"));
        assertEquals("text/plain", fileHelper.getMimeType("/recipe.txt"));
    }

    @Test
    public void getsNamesOfFilesInDirectory() {
        assertEquals(filenames, fileHelper.getFilenames(directory));
    }
}