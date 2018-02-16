package com.saralein.server.filesystem;

import com.saralein.server.range.Range;
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
        assertArrayEquals("1 cup rice".getBytes(), fileIO.readAllBytes(file));
    }

    @Test
    public void readsPartialFileContent() throws IOException {
        assertArrayEquals("1 cup rice".getBytes(), fileIO.readByteRange(file, new Range(0, 9)));
        assertArrayEquals("cup r".getBytes(), fileIO.readByteRange(file, new Range(2, 6)));
        assertArrayEquals("cup rice".getBytes(), fileIO.readByteRange(file, new Range(2, 9)));
        assertArrayEquals("p rice".getBytes(), fileIO.readByteRange(file, new Range(4, 9)));
    }

    @Test
    public void writesContentToFile() throws IOException {
        assertArrayEquals("1 cup rice".getBytes(), fileIO.readAllBytes(file));
        fileIO.write(file, "A pinch of salt");
        assertArrayEquals("A pinch of salt".getBytes(), fileIO.readAllBytes(file));
        fileIO.write(file, "1 cup rice");
        assertArrayEquals("1 cup rice".getBytes(), fileIO.readAllBytes(file));
    }
}
