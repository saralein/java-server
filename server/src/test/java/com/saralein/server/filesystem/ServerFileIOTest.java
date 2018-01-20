package com.saralein.server.filesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ServerFileIOTest {
    private Path file;
    private ServerFileIO serverFileIO;

    @Before
    public void setUp() {
        file = Paths.get(System.getProperty("user.dir"), "src/test/public/recipe.txt");
        serverFileIO = new ServerFileIO();
    }

    @After
    public void tearDown() throws IOException {
        Files.write(file, "1 cup rice".getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Test
    public void readsFullFile() throws IOException {
        assertArrayEquals("1 cup rice".getBytes(), serverFileIO.readFullFile(file));
    }

    @Test
    public void readsPartialFile() throws IOException {
        assertArrayEquals("1 cup ".getBytes(), serverFileIO.readPartialFile(file, 0, 5));
        assertArrayEquals("cup ri".getBytes(), serverFileIO.readPartialFile(file, 2, 7));
        assertArrayEquals("p rice".getBytes(), serverFileIO.readPartialFile(file, 4, 9));
    }

    @Test
    public void appendsToFile() throws IOException {
        String append = "\n2 cups water";
        String fileContents = new String(serverFileIO.readFullFile(file));
        serverFileIO.appendFile(file, append);
        String updatedContents = new String(serverFileIO.readFullFile(file));

        assertFalse(fileContents.contains(append));
        assertTrue(updatedContents.contains(append));
    }
}
