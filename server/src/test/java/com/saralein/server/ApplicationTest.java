package com.saralein.server;

import com.saralein.server.mocks.MockCallable;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class ApplicationTest {
    @Test
    public void callRunsApplicationCallable() {
        MockCallable mockCallable = new MockCallable();
        Application application = new Application(mockCallable);
        Request request = new Request.Builder()
                .method("GET")
                .uri("/recipe.txt")
                .build();
        Response response = application.call(request);
        assertArrayEquals("Callable response".getBytes(), response.getBody());
    }
}
