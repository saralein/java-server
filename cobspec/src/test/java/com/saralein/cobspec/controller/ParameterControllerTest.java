package com.saralein.cobspec.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParameterControllerTest {
    private static final String OK_HEADER = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n";
    private ParameterController parameterController;
    private Map<String, String> parameters;
    private String expectedHeader;
    private String expectedBody;

    public ParameterControllerTest(Map<String, String> parameters, String expectedHeader, String expectedBody) {
        this.parameters = parameters;
        this.expectedHeader = expectedHeader;
        this.expectedBody = expectedBody;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {
                        new HashMap<>(),
                        OK_HEADER,
                        ""
                },
                {
                        new HashMap<String, String>() {{
                            put("variable", "stuff");
                        }},
                        OK_HEADER,
                        "variable = stuff"
                },
                {
                        new HashMap<String, String>() {{
                            put("variable", "stuff");
                            put("another", "thing");
                        }},
                        OK_HEADER,
                        "another = thing\nvariable = stuff"
                }
        });
    }

    @Before
    public void setUp() {
        parameterController = new ParameterController();
    }

    @Test
    public void returnsCorrectResponseBasedOnUrl() {
        Request request = new Request.Builder()
                .uri("/parameters")
                .parameters(parameters)
                .build();
        Response response = parameterController.respond(request);
        Header header = response.getHeader();

        assertEquals(expectedHeader, header.formatToString());
        assertEquals(expectedBody, new String(response.getBody()));
    }
}
