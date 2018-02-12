package com.saralein.server.request.parser;

import java.util.HashMap;
import java.util.Map;

public class RangeParser {
    Map<String, Integer> parse(String range) {
        if (hasValidFormat(range)) {
            return mapRange(range);
        }

        return new HashMap<>();
    }

    private boolean hasValidFormat(String rangeLine) {
        return rangeLine.matches("^bytes=(?:(?:(\\d+-\\d+))|(?:\\d+-)|(?:-\\d+))$");
    }

    private Map<String, Integer> mapRange(String range) {
        String modifiedRange = removeRangeUnit(range);
        int hyphenIndex = modifiedRange.indexOf("-");

        if (hyphenIndex == modifiedRange.length() - 1) {
            return parseRangeFromStart(modifiedRange);
        }

        if (hyphenIndex == 0) {
            return parseRangeFromEnd(modifiedRange);
        }

        return parseFullRange(modifiedRange);

    }

    private String removeRangeUnit(String range) {
        return range.replace("bytes=", "");
    }

    private Map<String, Integer> parseRangeFromStart(String range) {
        int value = Integer.parseInt(range.substring(0, range.length() - 1));
        return new HashMap<String, Integer>() {{
            put("start", value);
        }};
    }

    private Map<String, Integer> parseRangeFromEnd(String range) {
        int value = Integer.parseInt(range.substring(1));
        return new HashMap<String, Integer>() {{
            put("end", value);
        }};
    }

    private Map<String, Integer> parseFullRange(String range) {
        String[] splitRange = range.split("-");

        return new HashMap<String, Integer>() {{
            put("start", Integer.parseInt(splitRange[0]));
            put("end", Integer.parseInt(splitRange[1]));
        }};
    }
}
