package com.saralein.server.validation;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DirectoryValidatorTest {
    private final String home = System.getProperty("user.dir");
    private final DirectoryValidator dirValidator = new DirectoryValidator(home);

    @Test
    public void returnsEmptyErrorListIfFlagNotInArgs() {
        List<String> args = Arrays.asList(new String[]{});
        ArrayList<String> errors = dirValidator.validate(args);

        assertTrue(errors.size() == 0);
    }

    @Test
    public void returnsCorrectErrorIfDirArgExcluded() {
        List<String> args = Arrays.asList(new String[]{"-d"});
        ArrayList<String> errors = dirValidator.validate(args);

        assertTrue(errors.contains("Valid directory option not provided.  Please include '-d' followed by a valid directory."));
    }

    @Test
    public void returnsCorrectErrorForInvalidDirArg() {
        List<String> nonExistentArgs = Arrays.asList(new String[]{"-d","coconuts"});
        ArrayList<String> nonExistentErrors = dirValidator.validate(nonExistentArgs);

        assertTrue(nonExistentErrors.contains("Valid directory not provided in options."));

        List<String> notDirArgs = Arrays.asList(new String[]{"-d","public/cheetara.jpg"});
        ArrayList<String> notDirErrors = dirValidator.validate(notDirArgs);

        assertTrue(notDirErrors.contains("Valid directory not provided in options."));
    }

    @Test
    public void returnsEmptyErrorListForValidDirOption() {
        List<String> args = Arrays.asList(new String[]{"-d","public"});
        ArrayList<String> errors = dirValidator.validate(args);

        assertTrue(errors.size() == 0);
    }
}