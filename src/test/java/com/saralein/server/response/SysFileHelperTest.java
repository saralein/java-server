package com.saralein.server.response;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SysFileHelperTest {
    SysFileHelper fileHelper;
    String userDir;
    List<String> filenames;
    String jpgPath = "/public/cheetara.jpg";
    String txtPath = "/public/recipe.txt";
    String gifPath = "/public/marshmallow.gif";

    @Before
    public void setUp() {
        userDir = System.getProperty("user.dir") + "/";
        fileHelper = new SysFileHelper("public");
        filenames = new ArrayList<>();

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
    public void getsNamesOfFilesInDirectory() {
        assertEquals(filenames, fileHelper.getFilenames());
    }
}