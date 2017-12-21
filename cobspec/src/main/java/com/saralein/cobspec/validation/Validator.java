package com.saralein.cobspec.validation;

import java.util.ArrayList;
import java.util.List;

public abstract class Validator {
    final ArrayList<String> errors;
    private final String flag;
    private final String argError;

    Validator(String flag, String argError) {
        this.errors = new ArrayList<>();
        this.flag = flag;
        this.argError = argError;
    }

    final ArrayList<String> validate(List<String> args) {
        int flagIndex = args.indexOf(flag);
        int argIndex = flagIndex + 1;

        if (flagProvided(flagIndex)) {
            if (argProvided(argIndex, args)) {
                String arg = args.get(argIndex);
                checkForErrors(arg);
            } else {
                errors.add(argError);
            }
        }

        return errors;
    }

    private boolean flagProvided(int flagIndex) {
        return flagIndex != -1;
    }

    private boolean argProvided(int argIndex, List<String> args) {
        return argIndex < args.size();
    }

    protected abstract void checkForErrors(String arg);
}
