package com.saralein.server.request.parser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParameterParser {
    private final String paramDelimiter;
    private final String nameValueDelimiter;
    private final String placeholder;

    public ParameterParser() {
        this.paramDelimiter = "&";
        this.nameValueDelimiter = "=";
        this.placeholder = "";
    }

    public Map<String, String> parse(String query) {
        Map<String, String> params = new HashMap<>();

        if (!query.isEmpty()) {
            params = mapParams(query);
        }

        return params;
    }

    private String[] splitMultipleParams(String params) {
        return params.split(paramDelimiter);
    }

    private String[] splitParamNameAndValue(String params) {
        return params.split(nameValueDelimiter, 2);
    }

    private Map<String, String> mapParams(String rawParameters) {
        return Arrays.stream(splitMultipleParams(rawParameters))
                .filter(this::isNotEmpty)
                .filter(this::hasValidName)
                .map(this::splitParamNameAndValue)
                .map(this::addPlaceholderForMissingValues)
                .map(this::decode)
                .collect(Collectors.toMap(variable -> variable.get(0), variable -> variable.get(1)));
    }

    private boolean isNotEmpty(String param) {
        return !param.isEmpty();
    }

    private boolean hasValidName(String param) {
        return param.indexOf(nameValueDelimiter) != 0;
    }

    private String[] addPlaceholderForMissingValues(String[] param) {
        if (param.length == 1) {
            return new String[]{param[0], placeholder};
        }

        return param;
    }

    private List<String> decode(String[] params) {
        return Arrays.stream(params)
                .map(this::decodeIfValidEncoding)
                .collect(Collectors.toList());
    }

    private String decodeIfValidEncoding(String encoded) {
        try {
            return URLDecoder.decode(encoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
