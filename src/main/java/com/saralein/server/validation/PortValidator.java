package com.saralein.server.validation;

import java.util.ArrayList;
import java.util.List;

public class PortValidator implements Validator {
    private final ArrayList<String> errors;
    private final String flag;
    private final String argError;

    public PortValidator() {
        this.errors = new ArrayList<>();
        this.flag = "-p";
        this.argError = "Valid port option not provided. Please include '-p' followed by a valid port.";
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

    private void checkForErrors(String portArg) {
        if (isNotNumeric(portArg) || isNotInRange(portArg)) {
            errors.add("Port must be a number between 1 and 65535.");
        }
    }

    private boolean isNotNumeric(String portArg) {
        return !portArg.matches("[-+]?\\d*\\.?\\d+");
    }

    private boolean isNotInRange(String portArg) {
        Integer portNum = Integer.parseInt(portArg);

        return 1 > portNum || portNum > 65535;
    }
}