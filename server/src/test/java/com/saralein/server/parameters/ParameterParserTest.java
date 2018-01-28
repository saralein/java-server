package com.saralein.server.parameters;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ParameterParserTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    private ParameterParser parameterParser;

    @Before
    public void setUp() {
        parameterParser = new ParameterParser();
    }

    @Test
    public void returnsEmptyParamsForUrlsWithoutParameters() throws Exception {
        Map<String, String> expected = new HashMap<>();

        assertEquals(expected, parameterParser.parse("/parameters?"));
        assertEquals(expected, parameterParser.parse("/parameters"));
    }

    @Test
    public void returnsMapForUrlsWithOneParameter() throws Exception {
        Map<String, String> expected = new HashMap<String, String>() {{
            put("variable", "stuff");
        }};

        assertEquals(expected, parameterParser.parse("/parameters?variable=stuff"));
    }

    @Test
    public void returnsMapForUrlsWithTwoParameters() throws Exception {
        Map<String, String> expected = new HashMap<String, String>() {{
            put("variable", "stuff");
            put("another", "thing");
        }};

        assertEquals(expected, parameterParser.parse("/parameters?variable=stuff&another=thing"));
    }

    @Test(expected = Exception.class)
    public void throwsForMissingParameterName() throws Exception {
        parameterParser.parse("/parameters?=stuff&another=thing");
    }

    @Test(expected = Exception.class)
    public void throwsForIncorrectParamDivision() throws Exception {
        parameterParser.parse("/parameters?variable=stuff&another=thing&");
    }

    @Test
    public void handlesValuelessParameters() throws Exception {
        Map<String, String> expected = new HashMap<String, String>() {{
            put("variable", "");
            put("another", "thing");
        }};

        assertEquals(expected, parameterParser.parse("/parameters?variable=&another=thing"));
        assertEquals(expected, parameterParser.parse("/parameters?variable&another=thing"));
    }

    @Test
    public void parsesComplicatedParameters() throws Exception {
        String uri = "/parameters?variable_1=Operators%20%3C%2C%20%3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-%2C%20*%2C" +
                "%20%26%2C%20%40%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F&variable_2=stuff";
        String paramValue = "Operators%20%3C%2C%20%3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-" +
                "%2C%20*%2C%20%26%2C%20%40%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F";
        Map<String, String> expected = new HashMap<String, String>() {{
            put("variable_1", paramValue);
            put("variable_2", "stuff");
        }};

        assertEquals(expected, parameterParser.parse(uri));
    }
}
