package com.saralein.server;

import java.io.File;
import java.util.Arrays;
import java.util.List;

class ArgsParser {
    private final List<String> args;

    ArgsParser(String[] args) {
        this.args = Arrays.asList(args);
    }

    Integer parsePort() {
        if (args.size() != 0) {
            int portIndex = args.indexOf("-p") + 1;
            return Integer.parseInt(args.get(portIndex));
        } else {
            return 5000;
        }
    }

    File parseRoot(String home) {
        if (args.size() != 0) {
            int portIndex = args.indexOf("-d") + 1;
            return new File (home + File.separator + args.get(portIndex));
        } else {
            return new File(home + File.separator + "public");
        }
    }
}
