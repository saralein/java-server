package com.saralein.server.filesystem;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.assertArrayEquals;

public class FileIOTest {
    private Path file;
    private FileIO fileIO;

    @Before
    public void setUp() {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        file = root.resolve("recipe.txt");
        fileIO = new FileIO();
    }

    @Test
    public void readsFileContent() throws IOException {
        assertArrayEquals("1 cup rice".getBytes(), fileIO.read(file));
    }

    @Test
    public void writesContentToFile() throws IOException {
        assertArrayEquals("1 cup rice".getBytes(), fileIO.read(file));
        fileIO.write(file, "A pinch of salt");
        assertArrayEquals("A pinch of salt".getBytes(), fileIO.read(file));
        fileIO.write(file, "1 cup rice");
        assertArrayEquals("1 cup rice".getBytes(), fileIO.read(file));
    }
}