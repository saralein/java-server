package com.saralein.server.request.parser;

import com.saralein.server.exchange.Cookie;
import com.saralein.server.exchange.RequestLine;
import com.saralein.server.request.Request;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

        List<String> messageHeaderAndBody = splitMessageHeaderAndBody(request);
        String rawRequestLine = pullRequestLine(request);
        String rawHeaders = pullHeaders(messageHeaderAndBody);
        String body = pullBody(messageHeaderAndBody);
        String cookieHeader = pullSingleHeader(request, "Cookie:\\s*(.*?)\r\n");
        String rangeHeader = pullSingleHeader(request, "Range:\\s*(.*?)\r\n");
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

    private String pullHeaders(List<String> messageHeaderAndBody) {
        String fullHeader = messageHeaderAndBody.get(0);

        if (fullHeader.contains(CRLF)) {
            int crlfIndex = fullHeader.indexOf(CRLF);
            return fullHeader.substring(crlfIndex);
        }

        return empty;
    }

    private String pullBody(List<String> messageHeaderAndBody) {
        int validLength = 2;

        if (messageHeaderAndBody.size() == validLength) {
            return messageHeaderAndBody.get(1);
        }

        return empty;
    }

    private String pullSingleHeader(String request, String toMatch) {
        String header = empty;
        Pattern pattern = Pattern.compile(toMatch);
        Matcher matcher = pattern.matcher(request);

        while (matcher.find()) {
            header = matcher.group(1);
        }

        return header;
    }

    private List<String> splitMessageHeaderAndBody(String request) {
        return Arrays.asList(request.split(CRLF + CRLF));
    }
}
