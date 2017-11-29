package com.saralein.server.validation;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PortValidatorTest {
    private final PortValidator portValidator = new PortValidator();

    @Test
    public void returnsEmptyErrorListIfFlagNotInArgs() {
        List<String> args = Arrays.asList(new String[]{});
        ArrayList<String> errors = portValidator.validate(args);

        assertTrue(errors.size() == 0);
    }

    @Test
    public void returnsCorrectErrorIfPortArgExcluded() {
        List<String> args = Arrays.asList(new String[]{"-p"});
        ArrayList<String> errors = portValidator.validate(args);

        assertTrue(errors.contains("Valid port option not provided. Please include '-p' followed by a valid port."));
    }

    @Test
    public void returnsCorrectErrorForInvalidPortArg() {
        List<String> stringPortArgs = Arrays.asList(new String[]{"-p","coconuts"});
        ArrayList<String> stringPortErrors = portValidator.validate(stringPortArgs);

        assertTrue(stringPortErrors.contains("Port must be a number between 1 and 65535."));

        List<String> outOfRangeArgs = Arrays.asList(new String[]{"-p","coconuts"});
        ArrayList<String> outOfRangeErrors = portValidator.validate(outOfRangeArgs);

        assertTrue(outOfRangeErrors.contains("Port must be a number between 1 and 65535."));
    }

    @Test
    public void returnsEmptyErrorListForValidPortOption() {
        List<String> args = Arrays.asList(new String[]{"-p","6066"});
        ArrayList<String> errors = portValidator.validate(args);

        assertTrue(errors.size() == 0);
    }
}