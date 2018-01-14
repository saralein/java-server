package com.saralein.server.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static com.saralein.server.Constants.CRLF;

public class RequestParser {
    public Request parse(String rawRequest) throws Exception {
        List<String> request = splitRawRequest(rawRequest);
        String[] requestLine = parseRequestLine(request);
        Map<String, String> headers = parseHeaders(request);
        String body = parseBody(request);

        return buildRequest(requestLine, headers, body);
    }

    private List<String> splitRawRequest(String rawRequest) {
       return Arrays.stream(rawRequest.split(CRLF))
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());
    }

    private String[] parseRequestLine(List<String> request) throws Exception {
        try {
            String requestLine = request.get(0);
            return requestLine.split(" ");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new Exception("Bad request. Connection closed.");
        }
    }

    private Map<String, String> parseHeaders(List<String> request) {
        return selectHeaderLines(request)
                .stream()
                .map(this::splitNameValue)
                .collect(Collectors.toMap(line -> line.get(0), line -> line.get(1)));
    }

    private List<String> selectHeaderLines(List<String> request) {
        return requestHasBody(request)
                ? new ArrayList<>(request.subList(1, request.size() - 1))
                : new ArrayList<>(request.subList(1, request.size()));
    }

    private boolean requestHasBody(List<String> request) {
        return request.stream()
                .filter(line -> !line.matches("Content-Length\\s*:\\s*0"))
                .anyMatch(line -> line.startsWith("Content-Length"));
    }

    private List<String> splitNameValue(String line) {
        return Arrays.asList(line.split("\\s*:\\s*"));
    }

    private String parseBody(List<String> request) {
        return requestHasBody(request)
                ? request.get(request.size() - 1)
                : "";
    }

    private Request buildRequest(String[] requestLine, Map<String, String> headers, String body) {
        return new Request.Builder()
                .method(getMethod(requestLine))
                .uri(getUri(requestLine))
                .body(body)
                .addHeaders(headers)
                .build();
    }

    private String getMethod(String[] requestLine) {
        return requestLine[0];
    }

    private String getUri(String[] requestLine) {
        return requestLine[1];
    }
}
