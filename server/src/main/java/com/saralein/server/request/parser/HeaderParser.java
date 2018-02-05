package com.saralein.server.request.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HeaderParser {
    public Map<String, String> parse(List<String> request) {
        return selectHeaderLines(request)
                .stream()
                .map(this::splitNameValue)
                .collect(Collectors.toMap(line -> line.get(0), line -> line.get(1)));
    }

    public boolean requestHasBody(List<String> request) {
        return request.stream()
                .filter(line -> !line.matches("Content-Length\\s*:\\s*0"))
                .anyMatch(line -> line.startsWith("Content-Length"));
    }

    private List<String> selectHeaderLines(List<String> request) {
        return requestHasBody(request)
                ? new ArrayList<>(request.subList(1, request.size() - 1))
                : new ArrayList<>(request.subList(1, request.size()));
    }

    private List<String> splitNameValue(String line) {
        return Arrays.asList(line.split("\\s*:\\s*"));
    }
}
