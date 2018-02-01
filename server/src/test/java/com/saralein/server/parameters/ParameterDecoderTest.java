package com.saralein.server.parameters;

import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ParameterDecoderTest {
    private ParameterDecoder parameterDecoder;

    @Before
    public void setUp() {
        parameterDecoder = new ParameterDecoder();
    }

    @Test
    public void doesNotAlterParametersWhichRequireNoDecoding() throws UnsupportedEncodingException {
        Map<String, String> parameter = new HashMap<String, String>() {{
            put("variable", "stuff");
            put("another", "thing");
        }};

        assertEquals(parameter, parameterDecoder.decode(parameter));
    }

    @Test
    public void decodesSimpleParameter() throws UnsupportedEncodingException {
        Map<String, String> parameter = new HashMap<String, String>() {{
            put("variable", "Hello%20there%2C%20friend!");
            put("another", "thing");
        }};

        Map<String, String> expected = new HashMap<String, String>() {{
            put("variable", "Hello there, friend!");
            put("another", "thing");
        }};

        assertEquals(expected, parameterDecoder.decode(parameter));
    }

    @Test
    public void decodesComplexParameter() throws UnsupportedEncodingException {
        String value = "Operators%20%3C%2C%20%3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-%2C%20*%2C%20" +
                "%26%2C%20%40%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F";

        Map<String, String> parameters = new HashMap<String, String>() {{
            put("variable_1", value);
            put("variable_2", "stuff");
        }};

        Map<String, String> expected = new HashMap<String, String>() {{
            put("variable_1", "Operators <, >, =, !=; +, -, *, &, @, #, $, [, ]: \"is that all\"?");
            put("variable_2", "stuff");
        }};

        assertEquals(expected, parameterDecoder.decode(parameters));
    }
}
