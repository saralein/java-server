package com.saralein.cobspec.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class ParameterControllerTest {
    private ParameterController parameterController;
    private Map<String, String> parameters;
    private Response expected;

    public ParameterControllerTest(Map<String, String> parameters, Response expected) {
        this.parameters = parameters;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {
                        new HashMap<>(),
                        new Response.Builder()
                                .status(200)
                                .addHeader("Content-Type", "text/plain")
                                .build()
                },
                {
                        new HashMap<String, String>() {{
                            put("variable", "stuff");
                        }},
                        new Response.Builder()
                                .status(200)
                                .addHeader("Content-Type", "text/plain")
                                .body("variable = stuff")
                                .build()
                },
                {
                        new HashMap<String, String>() {{
                            put("variable", "stuff");
                            put("another", "thing");
                        }},
                        new Response.Builder()
                                .status(200)
                                .addHeader("Content-Type", "text/plain")
                                .body("another = thing\nvariable = stuff")
                                .build()
                }
        });
    }

    @Before
    public void setUp() {
        parameterController = new ParameterController();
    }

    @Test
    public void returnsCorrectResponseBasedOnParameters() {
        Request request = new Request.Builder()
                .uri("/parameters")
                .parameters(parameters)
                .build();
        Response response = parameterController.call(request);

        assert (response.equals(expected));
    }
}
