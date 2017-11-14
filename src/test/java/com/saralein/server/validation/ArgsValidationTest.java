package com.saralein.server.validation;

import java.io.File;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArgsValidationTest {
    private ArgsValidation argsValidation;
    private String[] validArgs;
    private File root;

    @Before
    public void setUp() {
        String userLocation = System.getProperty("user.dir");
        String rootPath = userLocation + File.separator + "public";
        root = new File(rootPath);

        validArgs = new String[]{"6066", "public"};
        argsValidation = new ArgsValidation(userLocation);
    }

    @Test
    public void determinesIfArgumentsAreValid() {
        String[] invalidArgs = new String[]{"port6066", "totally-made-up"};
        String[] belowRange = new String[]{"-8000", "public"};
        String[] aboveRange = new String[]{"70000", "public"};

        assertTrue(argsValidation.argsAreValid(validArgs));
        assertFalse(argsValidation.argsAreValid(invalidArgs));
        assertFalse(argsValidation.argsAreValid(belowRange));
        assertFalse(argsValidation.argsAreValid(aboveRange));
    }

    @Test
    public void returnsDirectoryFromArgs() {
        argsValidation.argsAreValid(validArgs);
        assertEquals(root, argsValidation.determineRoot());
    }

    @Test
    public void returnsPortFromArgs() {
        argsValidation.argsAreValid(validArgs);
        assertEquals(new Integer(6066), argsValidation.parsePort());
    }
}