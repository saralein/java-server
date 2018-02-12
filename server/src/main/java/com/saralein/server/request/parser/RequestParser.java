package com.saralein.server.request.parser;

import com.saralein.server.exchange.Cookie;
import com.saralein.server.exchange.RequestLine;
import com.saralein.server.request.Request;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.saralein.server.Constants.CRLF;

public class RequestParser {
    private final RequestLineParser requestLineParser;
    private final HeaderParser headerParser;
    private final ParameterParser parameterParser;
    private final String empty;
    private final CookieParser cookieParser;
    private final RangeParser rangeParser;

    public RequestParser(
            RequestLineParser requestLineParser, HeaderParser headerParser,
            ParameterParser parameterParser, CookieParser cookieParser,
            RangeParser rangeParser
    ) {
        this.requestLineParser = requestLineParser;
        this.headerParser = headerParser;
        this.parameterParser = parameterParser;
        this.cookieParser = cookieParser;
        this.rangeParser = rangeParser;
        this.empty = "";
    }

    public Request parse(String request) throws Exception {
        if (request.isEmpty()) {
            throw new Exception("Invalid request.");
        }

        String rawRequestLine = pullRequestLine(request);
        List<String> rawHeaders = pullHeaders(request);
        String body = pullBody(request);
        String cookieHeader = pullHeaderValue(rawHeaders, "Cookie");
        String rangeHeader = pullHeaderValue(rawHeaders, "Range");
        RequestLine requestLine = requestLineParser.parse(rawRequestLine);

        String method = requestLine.getMethod();
        String uri = requestLine.getUri();
        String query = requestLine.getQuery();

        Map<String, String> headers = headerParser.parse(rawHeaders);
        Map<String, String> parameters = parameterParser.parse(query);
        List<Cookie> cookies = cookieParser.parse(cookieHeader);
        Map<String, Integer> range = rangeParser.parse(rangeHeader);

        return new Request.Builder()
                .method(method)
                .uri(uri)
                .body(body)
                .parameters(parameters)
                .cookies(cookies)
                .range(range)
                .addHeaders(headers)
                .build();
    }

    private String pullRequestLine(String request) {
        int crlfIndex = request.indexOf(CRLF);
        return request.substring(0, crlfIndex);
    }

    private List<String> pullHeaders(String request) {
        String withoutBody = request.split(CRLF + CRLF)[0];

        if (withoutBody.contains(CRLF)) {
            int crlfIndex = withoutBody.indexOf(CRLF);
            return Arrays.asList(withoutBody.substring(crlfIndex).split(CRLF));
        }

        return new ArrayList<>();
    }

    private String pullBody(String request) {
        String[] splitAtBody = request.split(CRLF + CRLF);
        int validLength = 2;

        if (splitAtBody.length == validLength) {
            return splitAtBody[1];
        }

        return empty;
    }

    private String pullHeaderValue(List<String> headers, String headerName) {
        for (String header : headers) {
            if (header.contains(headerName)) {
                return header.replaceAll(headerName + "\\s*:\\s*", "");
            }
        }

        return empty;
    }
}
