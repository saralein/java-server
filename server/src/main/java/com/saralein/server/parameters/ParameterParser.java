package com.saralein.server.parameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ParameterParser {
    public Map<String, String> parse(String uri) throws Exception {
        Map<String, String> params = new HashMap<>();
        String[] splitUriAndParams = splitUriAndParams(uri);

        if (splitUriAndParams.length > 1) {
            String rawParams = splitUriAndParams[1];
            params = formatParamsIfValid(rawParams);
        }

        return params;
    }

    private String[] splitUriAndParams(String uri) {
        return uri.split("\\?");
    }

    private Map<String, String> formatParamsIfValid(String rawParams) throws Exception {
        if (matchesParamPattern(rawParams)) {
            return mapParams(rawParams);
        } else {
            throw new Exception("Invalid parameter format.");
        }
    }

    private boolean matchesParamPattern(String param) {
        return param.matches("^(?:\\w+|\\w+=(\\w|\\pP[^&])*)(?:&(?:\\w+|\\w+=(\\w|\\pP[^&])*))*$");
    }

    private String[] splitMultipleParams(String params) {
        return params.split("&");
    }

    private String[] splitParamNameAndValue(String params) {
        return params.split("=");
    }

    private Map<String, String> mapParams(String rawParameters) {
        return Arrays.stream(splitMultipleParams(rawParameters))
                .map(this::splitParamNameAndValue)
                .map(this::addPlaceholderForMissingValues)
                .collect(Collectors.toMap(variable -> variable[0], variable -> variable[1]));
    }

    private String[] addPlaceholderForMissingValues(String[] param) {
        if (param.length == 1) {
            return new String[]{param[0], ""};
        }

        return param;
    }
}
