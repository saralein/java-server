package com.saralein.server.validation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryValidator implements Validator {
    private final String home;
    private final ArrayList<String> errors;
    private final String flag;
    private final String argError;

    public DirectoryValidator(String userHome) {
        this.home = userHome;
        this.errors = new ArrayList<>();
        this.flag = "-d";
        this.argError = "Valid directory option not provided.  Please include '-d' followed by a valid directory.";
    }

    public ArrayList<String> validate(List<String> args) {
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

    private void checkForErrors(String dirArg) {
        File dir = new File(home + File.separator + dirArg);

        if (!dir.exists() && !dir.isDirectory()) {
            errors.add("Valid directory not provided in options.");
        }
    }
}
