package com.saralein.server.request;

import com.saralein.server.parameters.ParameterDecoder;
import com.saralein.server.parameters.ParameterParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.saralein.server.Constants.CRLF;

public class RequestParser {
    private final ParameterParser parameterParser;
    private final ParameterDecoder parameterDecoder;

    public RequestParser(ParameterParser parameterParser, ParameterDecoder parameterDecoder) {
        this.parameterParser = parameterParser;
        this.parameterDecoder = parameterDecoder;
    }

    public Request parse(String rawRequest) throws Exception {
        List<String> request = splitRawRequest(rawRequest);
        String[] requestLine = parseRequestLine(request);
        Map<String, String> headers = parseHeaders(request);
        String body = parseBody(request);

        String uri = getUri(requestLine);
        Map<String, String> parameters = parameterParser.parse(uri);
        Map<String, String> decodedParameters = parameterDecoder.decode(parameters);
        String updatedUri = parameterParser.removeParamsFromUri(uri);


        return getRequest(updatedUri, getMethod(requestLine), headers, body, decodedParameters);
    }

    private List<String> splitRawRequest(String rawRequest) {
        return Arrays.stream(rawRequest.split(CRLF))
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toList());
    }

    private String[] parseRequestLine(List<String> request) throws Exception {
        String[] splitRequestLine = new String[]{};

        try {
            String requestLine = request.get(0);
            splitRequestLine = requestLine.split(" ");
        } catch (IndexOutOfBoundsException e) {
            throwException();
        }

        if (!validRequestLineLength(splitRequestLine)) {
            throwException();
        }

        return splitRequestLine;
    }

    private boolean validRequestLineLength(String[] requestLine) {
        return requestLine.length == 3;
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

    private Request getRequest(
            String uri, String method, Map<String, String> headers,
            String body, Map<String, String> parameters
    ) {
        return new Request.Builder()
                .method(method)
                .uri(uri)
                .body(body)
                .parameters(parameters)
                .addHeaders(headers)
                .build();
    }

    private String getMethod(String[] requestLine) {
        return requestLine[0];
    }

    private String getUri(String[] requestLine) {
        return requestLine[1];

    }

    private void throwException() throws Exception {
        throw new Exception("Bad request. Connection closed.");
    }
}