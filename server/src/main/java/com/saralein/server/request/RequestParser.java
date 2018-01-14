package com.saralein.server.request;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static com.saralein.server.Constants.CRLF;

public class RequestParser {
    public Request parse(String rawRequest) throws Exception {
        try {
            List<String> request = Arrays.asList(split(rawRequest, CRLF));
            String[] requestLine = parseRequestLine(request);

            return new Request.Builder()
                    .addMethod(parseMethod(requestLine))
                    .addUri(parseUri(requestLine))
                    .addBody(parseBody(request))
                    .addHeaders(parseHeaders(request))
                    .build();

        } catch(ArrayIndexOutOfBoundsException e) {
            throw new Exception("Bad request. Connection closed.");
        }
    }

    private String[] parseRequestLine(List<String> request) {
        return split(request.get(0), " ");
    }

    private String parseBody(List<String> request) {
        for (String line: request) {
            if (line.startsWith("body")) {
                return split(line, ":")[1].trim();
            }
        }

        return "";
    }

    private HashMap<String, String> parseHeaders(List<String> request) {
        HashMap<String, String> headers = new HashMap<>();

        request.stream()
                .filter(line -> line.contains(":") && !line.startsWith("body"))
                .map(line -> split(line, ":"))
                .forEach(line -> headers.put(line[0].trim(), line[1].trim()));

        return headers;
    }

    private String[] split(String request, String splitter) {
        return request.split(splitter);
    }

    private String parseMethod(String[] requestLine) {
        return requestLine[0];
    }

    private String parseUri(String[] requestLine) {
        return requestLine[1];
    }
}
