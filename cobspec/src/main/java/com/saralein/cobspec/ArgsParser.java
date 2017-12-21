package com.saralein.cobspec;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

class ArgsParser {
    private final List<String> args;
    private String separator = FileSystems.getDefault().getSeparator();

    ArgsParser(String[] args) {
        this.args = Arrays.asList(args);
    }

    Integer parsePort() {
        if (args.size() != 0 && args.contains("-p")) {
            int portIndex = args.indexOf("-p") + 1;
            return Integer.parseInt(args.get(portIndex));
        } else {
            return 5000;
        }
    }

    Path parseRoot(String home) {
        if (args.size() != 0 && args.contains("-d")) {
            int portIndex = args.indexOf("-d") + 1;
            return Paths.get(home + separator + args.get(portIndex));
        } else {
            return Paths.get(home + separator + "public");
        }
    }
}
