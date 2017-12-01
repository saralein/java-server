package com.saralein.server.request;

import static com.saralein.server.Constants.CRLF;
import java.util.HashMap;

public class RequestParser {
    private HashMap<String, String> parsedRequest;

    public RequestParser() {
        this.parsedRequest = new HashMap<>();
    }

    public Request parse(String request) throws Exception {
        try {
            String[] fullRequest = split(request, CRLF);
            createRequestMap(fullRequest);

            return new Request(parsedRequest);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new Exception("Bad request.  Connection closed.");
        }
    }

    private void createRequestMap(String[] fullRequest) {
        addRequestLine(fullRequest);

        if (fullRequest.length > 0) {
            addHeadersAndBody(fullRequest);
        }
    }

    private void addRequestLine(String[] fullRequest) {
        String[] requestLine = splitRequestLine(fullRequest);

        parsedRequest.put("method", parseMethod(requestLine));
        parsedRequest.put("uri", parseUri(requestLine));
        parsedRequest.put("version", parseVersion(requestLine));
    }

    private void addHeadersAndBody(String[] fullRequest) {
        for (String headerOrBody : fullRequest) {
            if (headerOrBody.contains(":")) {
                String[] splitHeaderOrBody = split(headerOrBody, ":");
                parsedRequest.put(splitHeaderOrBody[0].trim(), splitHeaderOrBody[1].trim());
            }
        }
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
