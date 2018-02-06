package com.saralein.server.request.parser;

import com.saralein.server.request.Request;
import com.saralein.server.request.transfer.RequestLine;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.saralein.server.Constants.CRLF;

public class RequestParser {
    private final RequestLineParser requestLineParser;
    private final HeaderParser headerParser;
    private final ParameterParser parameterParser;
    private final String empty;

    public RequestParser(
            RequestLineParser requestLineParser, HeaderParser headerParser, ParameterParser parameterParser
    ) {
        this.requestLineParser = requestLineParser;
        this.headerParser = headerParser;
        this.parameterParser = parameterParser;
        this.empty = "";
    }

    public Request parse(String request) throws Exception {
        if (request.isEmpty()) {
            throw new Exception("Invalid request.");
        }

        List<String> messageHeaderAndBody = splitMessageHeaderAndBody(request);
        String rawRequestLine = pullRequestLine(request);
        String rawHeaders = pullHeaders(messageHeaderAndBody);
        String body = pullBody(messageHeaderAndBody);
        RequestLine requestLine = requestLineParser.parse(rawRequestLine);

        String method = requestLine.getMethod();
        String uri = requestLine.getUri();
        String query = requestLine.getQuery();

        Map<String, String> headers = headerParser.parse(rawHeaders);
        Map<String, String> parameters = parameterParser.parse(query);

        return new Request.Builder()
                .method(method)
                .uri(uri)
                .body(body)
                .parameters(parameters)
                .addHeaders(headers)
                .build();
    }

    private String pullRequestLine(String request) {
        int crlfIndex = request.indexOf(CRLF);
        return request.substring(0, crlfIndex);
    }

    private String pullHeaders(List<String> messageHeaderAndBody) {
        String fullHeader = messageHeaderAndBody.get(0);
        int crlfIndex = fullHeader.indexOf(CRLF);

        if (crlfIndex != -1) {
            return fullHeader.substring(crlfIndex);
        }

        return empty;
    }

    private String pullBody(List<String> messageHeaderAndBody) {
        if (messageHeaderAndBody.size() == 2) {
            return messageHeaderAndBody.get(1);
        }

        return empty;
    }

    private List<String> splitMessageHeaderAndBody(String request) {
        return Arrays.asList(request.split(CRLF + CRLF));
    }
}
