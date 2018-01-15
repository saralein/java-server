package com.saralein.cobspec.controller;

import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OptionsControllerTest {
    private Request request;

    @Before
    public void setUp() {
        request = new Request(new HashMap<String, String>(){{
            put("method", "OPTIONS");
            put("uri", "/method_options");
            put("version", "HTTP/1.1");
        }});
    }

    @Test
    public void returnsResponseWithAllAllowedMethods() {
        OptionsController optionsController = new OptionsController(Methods.allowNonDestructiveMethods());
        Response response = optionsController.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nAllow: GET,OPTIONS,HEAD,POST,PUT\r\n\r\n", header.formatToString());
        assertArrayEquals("".getBytes(), response.getBody());
    }

    @Test
    public void returnsResponseWithGetOptionsMethods() {
        OptionsController optionsController = new OptionsController(Methods.allowGetAndOptions());
        Response response = optionsController.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nAllow: GET,OPTIONS\r\n\r\n", header.formatToString());
        assertArrayEquals("".getBytes(), response.getBody());
    }
}