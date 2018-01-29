package com.saralein.server.parameters;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class ParameterDecoder {
    public Map<String, String> decode(Map<String, String> params) throws UnsupportedEncodingException {
        Map<String, String> decodedParams = new HashMap<>();

        for (String key : params.keySet()) {
            String decodedKey = URLDecoder.decode(key, "UTF-8");
            String decodedValue = URLDecoder.decode(params.get(key), "UTF-8");
            decodedParams.put(decodedKey, decodedValue);
        }

        return decodedParams;
    }
}
