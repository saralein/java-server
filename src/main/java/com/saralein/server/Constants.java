package com.saralein.server;

import java.util.HashMap;

public final class Constants {
    private Constants() {}

    public static String CRLF = "\r\n";

    public static HashMap<String, String> STATUS_CODES = new HashMap<String, String>() {{
        put("200", "200 OK");
        put("404", "404 Not Found");
    }};
}
