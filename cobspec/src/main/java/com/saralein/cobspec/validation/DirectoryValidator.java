package com.saralein.cobspec.validation;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryValidator extends Validator {
    private final String home;
    private String separator = FileSystems.getDefault().getSeparator();

    public DirectoryValidator(String userHome) {
        super("-d", "Valid directory option not provided.  Please include '-d' followed by a valid directory.");
        this.home = userHome;
    }

    @Override
    protected void checkForErrors(String dirArg) {
        Path dir = Paths.get(home + separator + dirArg);

        if (!Files.exists(dir) && !Files.isDirectory(dir)) {
            errors.add("Valid directory not provided in options.");
        }
    }
}
