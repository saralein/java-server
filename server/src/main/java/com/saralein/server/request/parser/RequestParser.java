package com.saralein.server.request.parser;

import com.saralein.server.request.Request;
import com.saralein.server.request.transfer.RequestLine;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.saralein.server.Constants.CRLF;

public class RequestParser {
    private final RequestLineParser requestLineParser;
    private final HeaderParser headerParser;

    public RequestParser(RequestLineParser requestLineParser, HeaderParser headerParser) {
        this.requestLineParser = requestLineParser;
        this.headerParser = headerParser;
    }

    public Request parse(String rawRequest) throws Exception {
        if (rawRequest.isEmpty()) {
            throw new Exception("Bad request. Connection closed.");
        }

        List<String> request = splitRawRequest(rawRequest);
        RequestLine requestLine = requestLineParser.parse(request);
        Map<String, String> headers = headerParser.parse(request);
        String body = parseBody(request);

        return buildRequest(requestLine, headers, body);
    }

    private List<String> splitRawRequest(String rawRequest) {
        return Arrays.stream(rawRequest.split(CRLF))
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());
    }

    private String parseBody(List<String> request) {
        return headerParser.requestHasBody(request)
                ? request.get(request.size() - 1)
                : "";
    }

    private Request buildRequest(RequestLine requestLine, Map<String, String> headers, String body) {
        return new Request.Builder()
                .method(requestLine.getMethod())
                .uri(requestLine.getUri())
                .body(body)
                .addHeaders(headers)
                .build();
    }
}
