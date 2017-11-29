package com.saralein.server;

import java.nio.file.FileSystems;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
    private String home = System.getProperty("user.dir");
    private String separator = FileSystems.getDefault().getSeparator();

    @Test
    public void returnsDefaultPortIfNotProvided() {
        String[] args = new String[]{};
        ArgsParser argsParser = new ArgsParser(args);

        assertEquals(new Integer(5000), argsParser.parsePort());
    }
    @Test
    public void returnsPortIfProvided() {
        String[] args = new String[]{"-p", "1337"};
        ArgsParser argsParser = new ArgsParser(args);

        assertEquals(new Integer(1337), argsParser.parsePort());
    }

    @Test
    public void returnsDefaultDirectoryIfNotProvided() {
        String[] args = new String[]{};
        ArgsParser argsParser = new ArgsParser(args);
        String defaultPath = home + separator + "public";

        assertEquals(defaultPath, argsParser.parseRoot(home).toString());
    }

    @Test
    public void returnsDirectoryIfProvided() {
        String[] args = new String[]{"-d","sloths"};
        ArgsParser argsParser = new ArgsParser(args);
        String dirPath = home + separator + "sloths";

        assertEquals(dirPath, argsParser.parseRoot(home).toString());
    }
}