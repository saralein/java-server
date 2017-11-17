package com.saralein.server.validation;

import java.io.File;

public class DirectoryValidator extends Validator {
    private final String home;

    public DirectoryValidator(String userHome) {
        super("-d", "Valid directory option not provided.  Please include '-d' followed by a valid directory.");
        this.home = userHome;
    }

    @Override
    protected void checkForErrors(String dirArg) {
        File dir = new File(home + File.separator + dirArg);

        if (!dir.exists() && !dir.isDirectory()) {
            errors.add("Valid directory not provided in options.");
        }
    }
}
