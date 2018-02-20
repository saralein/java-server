package com.saralein.cobspec.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import org.junit.Test;

public class CoffeeControllerTest {
    @Test
    public void returns418ResponseWithBody() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("coffee")
                .build();
        Response expected = new ErrorResponse(418).respond();
        Response response = new CoffeeController().call(request);
        
        assert (response.equals(expected));
    }
}
