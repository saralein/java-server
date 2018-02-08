package com.saralein.server.request.parser;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ParameterParserTest {
    private ParameterParser parameterParser;

    @Before
    public void setUp() {
        parameterParser = new ParameterParser();
    }

    @Test
    public void returnsEmptyParamsForEmptyQuery() {
        Map<String, String> expected = new HashMap<>();

        assertEquals(expected, parameterParser.parse(""));
    }

    @Test
    public void returnsMapForUrlsWithOneParameter() {
        Map<String, String> expected = new HashMap<String, String>() {{
            put("variable", "stuff");
        }};

        assertEquals(expected, parameterParser.parse("variable=stuff"));
    }

    @Test
    public void returnsMapForUrlsWithTwoParameters() {
        Map<String, String> expected = new HashMap<String, String>() {{
            put("variable", "stuff");
            put("another", "thing");
        }};

        assertEquals(expected, parameterParser.parse("variable=stuff&another=thing"));
    }

    @Test
    public void handlesMissingParameterName() {
        Map<String, String> expected = new HashMap<String, String>() {{
            put("another", "thing");
        }};

        assertEquals(expected, parameterParser.parse("=stuff&another=thing"));
    }


    @Test
    public void handlesIncorrectParamDivision() {
        Map<String, String> expected = new HashMap<>();
        expected.put("variable", "stuff");

        assertEquals(expected, parameterParser.parse("variable=stuff&"));
        expected.put("another", "thing");
        assertEquals(expected, parameterParser.parse("variable=stuff&another=thing&"));
    }

    @Test
    public void handlesValuelessParameters() {
        Map<String, String> expected = new HashMap<String, String>() {{
            put("variable", "");
            put("another", "thing");
        }};

        assertEquals(expected, parameterParser.parse("variable=&another=thing"));
        assertEquals(expected, parameterParser.parse("variable&another=thing"));
    }

    @Test
    public void handlesExtraEqualsInParameter() {
        Map<String, String> expected = new HashMap<String, String>() {{
            put("variable", "stuff=thing");
        }};

        assertEquals(expected, parameterParser.parse("variable=stuff=thing"));
    }

    @Test
    public void handlesExtraParameterDeliminators() {
        Map<String, String> expected = new HashMap<String, String>() {{
            put("variable", "stuff");
            put("another", "thing");
        }};

        assertEquals(expected, parameterParser.parse("variable=stuff&&another=thing"));
    }

    @Test
    public void parsesComplicatedParameters() {
        String uri = "variable_1=Operators%20%3C%2C%20%3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-%2C%20*%2C" +
                "%20%26%2C%20%40%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F&variable_2=stuff";
        String paramValue = "Operators <, >, =, !=; +, -, *, &, @, #, $, [, ]: \"is that all\"?";
        Map<String, String> expected = new HashMap<String, String>() {{
            put("variable_1", paramValue);
            put("variable_2", "stuff");
        }};

        assertEquals(expected, parameterParser.parse(uri));
    }
}
