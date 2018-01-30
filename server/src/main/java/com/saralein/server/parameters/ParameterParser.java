package com.saralein.server.parameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ParameterParser {
    public Map<String, String> parse(String uri) {
        Map<String, String> params = new HashMap<>();
        String[] splitUriAndParams = splitUriAndParams(uri);

        if (splitUriAndParams.length > 1) {
            String rawParams = splitUriAndParams[1];
            params = mapParams(rawParams);
        }

        return params;
    }

    private String[] splitUriAndParams(String uri) {
        return uri.split("\\?");
    }

    private String[] splitMultipleParams(String params) {
        return params.split("&");
    }

    private String[] splitParamNameAndValue(String params) {
        return params.split("=", 2);
    }

    private Map<String, String> mapParams(String rawParameters) {
        return Arrays.stream(splitMultipleParams(rawParameters))
                .filter(this::isNotEmpty)
                .filter(this::hasValidName)
                .map(this::splitParamNameAndValue)
                .map(this::addPlaceholderForMissingValues)
                .collect(Collectors.toMap(variable -> variable[0], variable -> variable[1]));
    }

    private boolean isNotEmpty(String param) {
        return !param.isEmpty();
    }

    private boolean hasValidName(String param) {
        return param.indexOf("=") != 0;
    }

    private String[] addPlaceholderForMissingValues(String[] param) {
        if (param.length == 1) {
            return new String[]{param[0], ""};
        }

        return param;
    }
}
