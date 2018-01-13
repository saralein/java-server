package com.saralein.server.protocol;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Methods {
    GET, OPTIONS, HEAD, POST, PUT, DELETE;

    public static String allowNonDestructiveMethods() {
        return Arrays.stream(Methods.values())
                .filter(method -> method != Methods.DELETE)
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    public static String allowGetAndOptions() {
        return Methods.GET + "," + Methods.OPTIONS;
    }

    public static String allowedFileSystemMethods() {
        return Methods.GET + "," + Methods.HEAD;
    }
}
