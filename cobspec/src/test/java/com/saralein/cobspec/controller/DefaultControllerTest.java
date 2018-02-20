package com.saralein.cobspec.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Test;

public class DefaultControllerTest {
    @Test
    public void returns200Response() {
        Request request = new Request.Builder().build();
        Response expected = new Response.Builder().status(200).build();
        DefaultController defaultController = new DefaultController();
        Response response = defaultController.call(request);

        assert (response.equals(expected));
    }
}
