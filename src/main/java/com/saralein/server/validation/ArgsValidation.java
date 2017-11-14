package com.saralein.server.validation;

import java.io.File;

public class ArgsValidation {
    private String userLocation;
    private String port;
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
        setUpArgs(args);

        return validPort() && validDirectory();
    }

    private void setUpArgs(String[] args) {
        if(args.length == 0) {
            setupPortAndRoot("1337", "Public");
        } else {
            setupPortAndRoot(args[0], args[1]);
        }
    }

    private void setupPortAndRoot(String argPort, String argRoot) {
        port = argPort;
        root = new File(userLocation + File.separator + argRoot);
    }

    private boolean validDirectory() {
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
