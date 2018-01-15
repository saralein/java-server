package com.saralein.cobspec.controller;

import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsControllerTest {
    private Request request;

    @Before
    public void setUp() {
        request = new Request.Builder()
                .addMethod("OPTIONS")
                .addUri("/method_options")
                .build();
    }

    @Test
    public void returnsResponseWithAllAllowedMethods() {
        OptionsController optionsController = new OptionsController(Methods.allowNonDestructiveMethods());
        Response response = optionsController.createResponse(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nAllow: GET,OPTIONS,HEAD,POST,PUT\r\n\r\n", header.formatToString());
        assertArrayEquals("".getBytes(), response.getBody());
    }

    @Test
    public void returnsResponseWithGetOptionsMethods() {
        OptionsController optionsController = new OptionsController(Methods.allowGetAndOptions());
        Response response = optionsController.createResponse(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nAllow: GET,OPTIONS\r\n\r\n", header.formatToString());
        assertArrayEquals("".getBytes(), response.getBody());
    }
}