package com.saralein.cobspec.validation;

public class PortValidator extends Validator {
    public PortValidator() {
        super("-p", "Valid port option not provided. Please include '-p' followed by a valid port.");
    }

    @Override
    protected void checkForErrors(String portArg) {
        if (isNotNumeric(portArg) || isNotInRange(portArg)) {
            errors.add("Port must be a number between 1 and 65535.");
        }
    }

    private boolean isNotNumeric(String portArg) {
        return !portArg.matches("[-+]?\\d*\\.?\\d+");
    }

    private boolean isNotInRange(String portArg) {
        Integer portNum = Integer.parseInt(portArg);

        return 1024 >= portNum || portNum > 65535;
    }
}