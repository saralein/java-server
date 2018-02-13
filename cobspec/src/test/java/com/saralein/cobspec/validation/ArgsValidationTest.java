package com.saralein.cobspec.validation;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;

public class ArgsValidationTest {
    private ArgsValidation argsValidation;

    @Before
    public void setUp() {
        String home = System.getProperty("user.dir") + "/src/test";

        List<Validator> validators = new ArrayList<Validator>() {{
            add(new PortValidator());
            add(new DirectoryValidator(home));
        }};

        argsValidation = new ArgsValidation(validators);
    }

    @Test
    public void callsAllGiveValidators() {
        String[] args = new String[]{"-d", "cheese", "-p", "coconuts"};
        List<String> errors = argsValidation.validate(args);

        assertTrue(errors.contains("Valid directory not provided in options."));
        assertTrue(errors.contains("Port must be a number between 1024 and 65535."));
    }

    @Test
    public void returnsEmptyErrorListIsOptionsAreValid() {
        String[] args = new String[]{"-d", "public", "-p", "6066"};
        List<String> errors = argsValidation.validate(args);

        assertTrue(errors.size() == 0);
    }
}
