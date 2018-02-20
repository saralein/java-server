package com.saralein.cobspec.controller;

import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;

public class OptionsControllerTest {
    private Request request;

    private Response responseWithAllowedMethods(String methods) {
        return new Response.Builder()
                .status(200)
                .addHeader("Allow", methods)
                .build();
    }

    @Before
    public void setUp() {
        request = new Request.Builder()
                .method("OPTIONS")
                .uri("/method_options")
                .build();
    }

    @Test
    public void returnsResponseWithAllAllowedMethods() {
        Response expected = responseWithAllowedMethods("GET,OPTIONS,HEAD,POST,PUT");
        OptionsController optionsController = new OptionsController(Methods.allowAllButDeleteAndPatch());
        Response response = optionsController.call(request);

        assert (response.equals(expected));
    }

    @Test
    public void returnsResponseWithGetOptionsMethods() {
        Response expected = responseWithAllowedMethods("GET,OPTIONS");
        OptionsController optionsController = new OptionsController(Methods.allowGetAndOptions());
        Response response = optionsController.call(request);

        assert (response.equals(expected));
    }
}
