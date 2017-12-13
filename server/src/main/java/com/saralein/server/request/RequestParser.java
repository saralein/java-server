package com.saralein.server.request;

import static com.saralein.server.Constants.CRLF;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RequestParser {
    public Request parse(String request) throws Exception {
        try {
            List<String> fullRequest = Arrays.asList(split(request, CRLF));
            HashMap<String, String> parsedRequest = createRequestMap(fullRequest);
            return new Request(parsedRequest);
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new Exception("Bad request. Connection closed.");
        }
    }

    private HashMap<String, String> createRequestMap(List<String> fullRequest) {
        HashMap<String, String> parsedRequest = addRequestLine(fullRequest);

        if (fullRequest.size() > 0) {
            parsedRequest.putAll(addHeadersAndBody(fullRequest));
        }

        return parsedRequest;
    }

    private HashMap<String, String> addRequestLine(List<String> fullRequest) {
        String[] requestLine = splitRequestLine(fullRequest);

        return new HashMap<String, String>(){{
            put("method", parseMethod(requestLine));
            put("uri", parseUri(requestLine));
            put("version", parseVersion(requestLine));
        }};
    }

    private HashMap<String, String> addHeadersAndBody(List<String> fullRequest) {
        HashMap<String, String> headersAndBody = new HashMap<>();

        fullRequest.stream()
                .filter(line -> line.contains(":"))
                .map(line -> split(line, ":"))
                .forEach(line -> headersAndBody.put(line[0].trim(), line[1].trim()));

        return headersAndBody;
    }

    private String[] split(String request, String splitter) {
        return request.split(splitter);
    }

    private String[] splitRequestLine(List<String> fullRequest) {
        return split(fullRequest.get(0), " ");
    }

    private String parseMethod(String[] requestLine) {
        return requestLine[0];
    }

    private String parseUri(String[] requestLine) {
        return requestLine[1];
    }

    private String parseVersion(String[] requestLine) {
        return requestLine[2];
    }
}
