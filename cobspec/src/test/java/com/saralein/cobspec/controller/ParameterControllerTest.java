package com.saralein.cobspec.controller;

import com.saralein.server.parameters.ParameterDecoder;
import com.saralein.server.parameters.ParameterParser;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParameterControllerTest {
    private static final String OK_HEADER = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n";
    private ParameterController parameterController;
    private String uri;
    private String expectedHeader;
    private String expectedBody;

    public ParameterControllerTest(String uri, String expectedHeader, String expectedBody) {
        this.uri = uri;
        this.expectedHeader = expectedHeader;
        this.expectedBody = expectedBody;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {
                        "/parameters",
                        OK_HEADER,
                        ""
                },
                {
                        "/parameters?variable=stuff",
                        OK_HEADER,
                        "variable = stuff"
                },
                {
                        "/parameters?variable=stuff&another=thing",
                        OK_HEADER,
                        "another = thing\nvariable = stuff"
                },
                {
                        "/parameters?variable_1=Operators%20%3C%2C" +
                        "%20%3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-" +
                        "%2C%20*%2C%20%26%2C%20%40%2C%20%23%2C%20%24%2C" +
                        "%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F&variable_2=stuff",
                        OK_HEADER,
                        "variable_1 = Operators <, >, =, !=; +, -, *, &, @, #, $, [, ]: \"is that all\"?\n" +
                                "variable_2 = stuff"
                },
                {
                        "/parameters?=stuff",
                        OK_HEADER,
                        ""
                }
        });
    }

    @Before
    public void setUp() {
        parameterController = new ParameterController(new ParameterParser(), new ParameterDecoder());
    }

    @Test
    public void returnsCorrectResponseBasedOnUrl() {
        Request request = new Request.Builder()
                .uri(uri)
                .build();
        Response response = parameterController.respond(request);
        Header header = response.getHeader();

        assertEquals(expectedHeader, header.formatToString());
        assertEquals(expectedBody, new String(response.getBody()));
    }
}
