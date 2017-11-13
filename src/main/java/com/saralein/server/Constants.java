package com.saralein.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Constants {
    private Constants() {}

    public static final String CRLF = "\r\n";

    private static final HashMap<Integer, String> modifiableStatusCodes = new HashMap<Integer, String>() {{
        put(200, "200 OK");
        put(404, "404 Not Found");
    }};

    public static final Map<Integer, String> STATUS_CODES = Collections.unmodifiableMap(modifiableStatusCodes);
}
