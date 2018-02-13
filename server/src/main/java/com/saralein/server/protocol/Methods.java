package com.saralein.server.protocol;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Methods {
    GET, OPTIONS, HEAD, PATCH, POST, PUT, DELETE;

    public static String allowAllButDeleteAndPatch() {
        return Arrays.stream(Methods.values())
                .filter(Methods::isNotDeleteOrPatch)
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    public static String allowGetAndOptions() {
        return Methods.GET + "," + Methods.OPTIONS;
    }

    public static String allowedFileMethods() {
        return Methods.GET + "," + Methods.HEAD;
    }

    private static boolean isNotDeleteOrPatch(Methods method) {
        return method != Methods.DELETE && method != Methods.PATCH;
    }
}
