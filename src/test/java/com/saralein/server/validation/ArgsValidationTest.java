package com.saralein.server.validation;

import java.io.File;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArgsValidationTest {
    private ArgsValidation argsValidation;
    private String[] emptyArgs;
    private String[] validArgs;
    private File root;

    @Before
    public void setUp() {
        String userLocation = System.getProperty("user.dir");
        String rootPath = userLocation + File.separator + "public";
        root = new File(rootPath);

        emptyArgs = new String[]{};
        validArgs = new String[]{"-p", "6066", "-d", "public"};
        argsValidation = new ArgsValidation(userLocation);
    }

    @Test
    public void returnsFalseWhenArgsHaveFlagsWithMissingSpecification() {
        String[] justDir = new String[]{"-d"};
        String[] justPort = new String[]{"-p"};
        String[] missingPortFirst = new String[]{"-d", "sloths", "-p"};
        String[] missingPortLast = new String[]{"-p", "-d", "sloths"};
        String[] missingDirectoryFirst = new String[]{"-d", "-p", "6066"};
        String[] missingDirectoryLast = new String[]{"-p", "6066", "-d"};

        assertFalse(argsValidation.argsAreValid(justDir));
        assertFalse(argsValidation.argsAreValid(justPort));
        assertFalse(argsValidation.argsAreValid(missingPortFirst));
        assertFalse(argsValidation.argsAreValid(missingPortLast));
        assertFalse(argsValidation.argsAreValid(missingDirectoryFirst));
        assertFalse(argsValidation.argsAreValid(missingDirectoryLast));
    }

    @Test
    public void determinesIfArgsWithValidFormatHaveValidSpecifications() {
        String[] onlyValidPort = new String[]{"-p", "6060"};
        String[] onlyValidDir = new String[]{"-d", "public"};

        String[] invalidSpecs = new String[]{"-p", "port6066", "-d", "totally-made-up"};
        String[] flagsFirst = new String[]{"-p", "-d", "6060", "public"};
        String[] flagsLast = new String[]{ "6060", "public", "-p", "-d"};
        String[] belowRangePort = new String[]{"-p", "-8000", "-d", "public"};
        String[] aboveRangePort = new String[]{"-p", "70000", "-d", "public"};
        String[] onlyInvalidPort = new String[]{"-p", "70000"};
        String[] onlyInvalidDir = new String[]{"-d", "-p"};

        assertTrue(argsValidation.argsAreValid(validArgs));
        assertTrue(argsValidation.argsAreValid(emptyArgs));
        assertTrue(argsValidation.argsAreValid(onlyValidPort));
        assertTrue(argsValidation.argsAreValid(onlyValidDir));

        assertFalse(argsValidation.argsAreValid(invalidSpecs));
        assertFalse(argsValidation.argsAreValid(flagsFirst));
        assertFalse(argsValidation.argsAreValid(flagsLast));
        assertFalse(argsValidation.argsAreValid(belowRangePort));
        assertFalse(argsValidation.argsAreValid(aboveRangePort));
        assertFalse(argsValidation.argsAreValid(onlyInvalidPort));
        assertFalse(argsValidation.argsAreValid(onlyInvalidDir));
    }

    @Test
    public void returnsDirectoryFromArgs() {
        argsValidation.argsAreValid(emptyArgs);
        assertEquals(root, argsValidation.determineRoot());

        argsValidation.argsAreValid(validArgs);
        assertEquals(root, argsValidation.determineRoot());
    }

    @Test
    public void returnsPortFromArgs() {
        argsValidation.argsAreValid(emptyArgs);
        assertEquals(new Integer(1337), argsValidation.parsePort());

        argsValidation.argsAreValid(validArgs);
        assertEquals(new Integer(6066), argsValidation.parsePort());
    }
}