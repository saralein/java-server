package com.saralein.server.filesystem;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ServerFilesTest {
    private Path jpgPath;
    private Path pdfPath;
    private Path txtPath;
    private Path gifPath;
    private Path root;
    private ServerFiles serverFiles;
    private List<String> fileNames;

    @Before
    public void setUp() {
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        jpgPath = root.resolve("cheetara.jpg");
        pdfPath = root.resolve("cake.pdf");
        txtPath = root.resolve("recipe.txt");
        gifPath = root.resolve("marshmallow.gif");
        serverFiles = new ServerFiles();
        fileNames = new ArrayList<>();

        fileNames.add("cake.pdf");
        fileNames.add("cheetara.jpg");
        fileNames.add("marshmallow.gif");
        fileNames.add("recipe.txt");
        fileNames.add("sloths/");
    }

    @Test
    public void getsMimeTypesOfFiles() {
        assertEquals("application/pdf", serverFiles.determineMimeType("/cake.pdf"));
        assertEquals("image/jpeg", serverFiles.determineMimeType("/cheetara.jpg"));
        assertEquals("image/gif", serverFiles.determineMimeType("/marshmallow.gif"));
        assertEquals("text/plain", serverFiles.determineMimeType("/recipe.txt"));
    }

    @Test
    public void getsLengthOfFiles() throws IOException {
        assertEquals(10, serverFiles.getFileLength(txtPath));
        assertEquals(21831251, serverFiles.getFileLength(pdfPath));
        assertEquals(15587, serverFiles.getFileLength(jpgPath));
        assertEquals(2628712, serverFiles.getFileLength(gifPath));
    }

    @Test
    public void getsNamesOfFilesInDirectory() throws IOException {
        assertEquals(fileNames, serverFiles.listFileNames(root));
    }
}
