package com.saralein.server.parameters;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class ParameterDecoder {
    public Map<String, String> decode(Map<String, String> params) {
        Map<String, String> decodedParams = new HashMap<>();

        for (String key : params.keySet()) {
            String decodedKey = decodeIfValidEncoding(key);
            String decodedValue = decodeIfValidEncoding(params.get(key));
            decodedParams.put(decodedKey, decodedValue);
        }

        return decodedParams;
    }

    private String decodeIfValidEncoding(String encoded) {
        try {
            return URLDecoder.decode(encoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
