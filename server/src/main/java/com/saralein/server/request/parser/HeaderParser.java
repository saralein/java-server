package com.saralein.server.request.parser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HeaderParser {
    public Map<String, String> parse(List<String> headers) {
        return headers.stream()
                .filter(header -> !header.isEmpty())
                .map(this::splitNameValue)
                .collect(Collectors.toMap(line -> line.get(0), line -> line.get(1)));
    }

    private List<String> splitNameValue(String line) {
        return Arrays.asList(line.split("\\s*:\\s*"));
    }
}
