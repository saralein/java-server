package com.saralein.server.filesystem;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ServerFileIOTest {
    private final int start;
    private final int end;
    private final String expected;
    private Path txtPath;
    private FileIO fileIO;

    public ServerFileIOTest(int start, int end, String expected) {
        this.start = start;
        this.end = end;
        this.expected = expected;
    }

    @Before
    public void setUp() {
        txtPath = Paths.get(System.getProperty("user.dir"), "src/test/public", "recipe.txt");
        fileIO = new ServerFileIO();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {0, 9, "1 cup rice"},
                {2, 6, "cup r"},
                {2, 9, "cup rice"},
                {4, 9, "p rice"}
        });
    }

    @Test
    public void returnsPartialFileForVariedRanges() throws IOException {
        byte[] bytes = fileIO.readPartialFile(txtPath, start, end);

        assertArrayEquals(expected.getBytes(), bytes);
    }
}