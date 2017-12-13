package com.saralein.server.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgsValidation {
    private final List<Validator> validators;
    private List<String> errors;

    public ArgsValidation(List<Validator> validators) {
        this.errors = new ArrayList<>();
        this.validators = validators;
    }

    public List<String> validate(String[] args) {
        List<String> argsList = Arrays.asList(args);

        if (args.length != 0) {
            for (Validator validator : validators) {
                errors.addAll(validator.validate(argsList));
            }
        }

        return errors;
    }
}