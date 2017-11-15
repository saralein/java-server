package com.saralein.server.validation;

import static com.saralein.server.Constants.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ArgsValidation {
    private String userLocation;
    private String port;
    private String rootPath;
    private File root;

    public ArgsValidation(String userLocation) {
        this.userLocation = userLocation;
    }

    public Integer parsePort() {
        return Integer.parseInt(port);
    }

    public File determineRoot() {
        return root;
    }

    public boolean argsAreValid(String[] args) {
        List<String> argsList = Arrays.asList(args);

        if (hasValidArgsFormat(argsList)) {
            setupArgs(argsList);
        } else {
            return false;
        }

        return validPort() && validDirectory();
    }

    private boolean hasValidArgsFormat(List<String> argsList) {
        return argsHaveNoLength(argsList) || hasBothFlagsAndValidLength(argsList) || hasOneFlagAndValidLength(argsList);
    }

    private boolean argsHaveNoLength(List<String> argsList) {
        return argsList.size() == 0;
    }

    private boolean hasBothFlagsAndValidLength(List<String> argsList) {
        return hasFlag(argsList, PORT_FLAG) && hasFlag(argsList, DIRECTORY_FLAG) && argsList.size() == EXPECTED_ARGS_LENGTH;
    }

    private boolean hasOneFlagAndValidLength(List<String> argsList) {
        return (hasFlag(argsList, PORT_FLAG) || hasFlag(argsList, DIRECTORY_FLAG)) &&
                argsList.size() == EXPECTED_ARGS_LENGTH / 2;
    }

    private boolean hasFlag(List<String> argsList, String flag) {
        return argsList.indexOf(flag) == 0 || argsList.indexOf(flag) == 2;
    }

    private void setupArgs(List<String> argsList) {
        port = setupArg(argsList, PORT_FLAG);
        rootPath = setupArg(argsList, DIRECTORY_FLAG);
    }

    private String setupArg(List<String> argsList, String flag) {
        int flagIndex = argsList.indexOf(flag);
        int argIndex = flagIndex + 1;

        if(flagIndex != -1 && argIndex < argsList.size()) {
            return argsList.get(argIndex);
        } else {
            return flag == PORT_FLAG ? "1337" : "public";
        }
    }

    private boolean validDirectory() {
        root = new File(userLocation + File.separator + rootPath);

        return root.exists();
    }

    private boolean validPort() {
        return isNumeric() && isInRange();
    }

    private boolean isInRange() {
        Integer portNum = Integer.parseInt(port);
        return 1 <= portNum && portNum <= 65535;
    }

    private boolean isNumeric() {
        return port != null && port.matches("[-+]?\\d*\\.?\\d+");
    }
}
