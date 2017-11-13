package com.saralein.server.request;

import static com.saralein.server.Constants.CRLF;
import java.util.HashMap;

public class RequestParser {
    public Request parse(String request) {
        String[] fullRequest = split(request, CRLF);
        String[] requestLine = splitRequestLine(fullRequest);
        HashMap<String, String> parsedRequest = createRequestMap(requestLine);

        return new Request(parsedRequest);
    }

    private HashMap<String, String> createRequestMap(String[] requestLine) {
        HashMap<String, String> parsedRequest = new HashMap<>();

        parsedRequest.put("method", parseMethod(requestLine));
        parsedRequest.put("uri", parseUri(requestLine));
        parsedRequest.put("version", parseVersion(requestLine));

        return parsedRequest;
    }

    private String[] split(String request, String splitter) {
        return request.split(splitter);
    }

    private String[] splitRequestLine(String[] fullRequest) {
        return split(fullRequest[0], " ");
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
