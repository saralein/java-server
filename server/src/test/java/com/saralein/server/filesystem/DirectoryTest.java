package com.saralein.server.filesystem;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class DirectoryTest {
    private Path root;
    private List<String> fileNames;
    private Directory directory;

    @Before
    public void setUp() {
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        directory = new Directory();
        fileNames = new ArrayList<>();
        fileNames.add("cake.pdf");
        fileNames.add("cheetara.jpg");
        fileNames.add("marshmallow.gif");
        fileNames.add("recipe.txt");
        fileNames.add("sloths/");
    }

    @Test
    public void getsNamesOfFilesInDirectory() throws IOException {
        assertEquals(fileNames, directory.listFileNames(root));
    }
}
