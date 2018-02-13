package com.saralein.cobspec.validation;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertTrue;

public class PortValidatorTest {
    private PortValidator portValidator;

    @Before
    public void setUp() {
        portValidator = new PortValidator();
    }

    @Test
    public void returnsEmptyErrorListIfFlagNotInArgs() {
        List<String> args = Collections.emptyList();
        ArrayList<String> errors = portValidator.validate(args);

        assertTrue(errors.size() == 0);
    }

    @Test
    public void returnsCorrectErrorIfPortArgExcluded() {
        List<String> args = Collections.singletonList("-p");
        ArrayList<String> errors = portValidator.validate(args);

        assertTrue(errors.contains("Valid port option not provided. Please include '-p' followed by a valid port."));
    }

    @Test
    public void returnsCorrectErrorForInvalidPortArg() {
        String error = "Port must be a number between 1024 and 65535.";
        List<String> nonNumericPortArg = Arrays.asList("-p", "coconuts");
        ArrayList<String> nonNumericPortError = portValidator.validate(nonNumericPortArg);

        assertTrue(nonNumericPortError.contains(error));

        List<String> outOfRangeArg = Arrays.asList("-p", "23");
        ArrayList<String> outOfRangeError = portValidator.validate(outOfRangeArg);

        assertTrue(outOfRangeError.contains(error));
    }

    @Test
    public void returnsEmptyErrorListForValidPortOption() {
        List<String> args = Arrays.asList("-p", "6066");
        ArrayList<String> errors = portValidator.validate(args);

        assertTrue(errors.size() == 0);
    }
}
