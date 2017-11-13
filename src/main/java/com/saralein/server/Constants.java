package com.saralein.server;

import java.util.HashMap;

public final class Constants {
    private Constants() {}

    public static final String CRLF = "\r\n";

    public static final HashMap<Integer, String> STATUS_CODES = new HashMap<Integer, String>() {{
        put(200, "200 OK");
        put(404, "404 Not Found");
    }};
}
