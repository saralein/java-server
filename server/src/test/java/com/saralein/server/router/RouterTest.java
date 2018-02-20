package com.saralein.server.router;

import com.saralein.server.mocks.MockController;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;

public class RouterTest {
    private Router router;

    @Before
    public void setUp() {
        Routes routes = new Routes().get("/stuff", new MockController(200, "Some stuff!"));
        router = new Router(routes);
    }

    private Request createRequest(String method, String uri) {
        return new Request.Builder()
                .method(method)
                .uri(uri)
                .build();
    }

    @Test
    public void returns200ResponseForMatchingRoute() {
        Response expected = new Response.Builder()
                .status(200)
                .body("Some stuff!")
                .build();
        Request request = createRequest("GET", "/stuff");
        Response response = router.call(request);

        assert (response.equals(expected));
    }

    @Test
    public void returnsNotFoundResponseForNonExistentResources() {
        Response expected = new ErrorResponse(404).respond();
        Request request = createRequest("GET", "/snarf.jpg");
        Response response = router.call(request);

        assert (response.equals(expected));
    }

    @Test
    public void returnsNotAllowedForIncorrectMethodOnResource() {
        Response expected = new ErrorResponse(405).respond("Allow", "GET");
        Request request = createRequest("DELETE", "/stuff");
        Response response = router.call(request);

        assert (response.equals(expected));
    }
}
