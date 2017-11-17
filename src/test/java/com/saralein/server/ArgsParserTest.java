package com.saralein.server;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ArgsParserTest {
    private String home = System.getProperty("user.dir");

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
        String defaultPath = home + File.separator + "public";

        assertEquals(defaultPath, argsParser.parseRoot(home).getAbsolutePath());
    }

    @Test
    public void returnsDirectoryIfProvided() {
        String[] args = new String[]{"-d","sloths"};
        ArgsParser argsParser = new ArgsParser(args);
        String dirPath = home + File.separator + "sloths";

        assertEquals(dirPath, argsParser.parseRoot(home).getAbsolutePath());
    }
}