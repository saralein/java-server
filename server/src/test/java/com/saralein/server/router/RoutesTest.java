package com.saralein.server.router;

import com.saralein.server.controller.*;
import com.saralein.server.mocks.MockController;
import com.saralein.server.mocks.MockErrorController;
import com.saralein.server.mocks.MockRedirect;
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class RoutesTest {
    private MockController directoryController = new MockController(200, "Directory response");
    private MockController fileController = new MockController(200, "File response");
    private MockErrorController notFoundController = new MockErrorController(404, "Not found response");

    private HashMap<String, Controller> redirect = new HashMap<String, Controller>(){{
        put("GET", new MockRedirect());
    }};
    private HashMap<String, HashMap<String, Controller>> customRoutes =
            new HashMap<String, HashMap<String, Controller>>() {{
                put("/redirect", redirect);
            }};
    private Routes routes = new Routes(customRoutes, directoryController, fileController, notFoundController);

    @Test
    public void checksForMatchingRouteAndMethod() {
        assertFalse(routes.matchesRouteAndMethod("/doggos", "GET"));
        assertFalse(routes.matchesRouteAndMethod("/redirect", "DELETE"));
        assertTrue(routes.matchesRouteAndMethod("/redirect", "GET"));
    }

    @Test
    public void checksForMatchingRouteWithoutMatchingMethod() {
        assertFalse(routes.matchesRouteButNotMethod("/doggos", "GET"));
        assertFalse(routes.matchesRouteButNotMethod("/redirect", "GET"));
        assertTrue(routes.matchesRouteButNotMethod("/redirect", "DELETE"));
    }

    @Test
    public void returnsControllerForRoute() {
        assertEquals(MockRedirect.class, routes.retrieveController("/redirect", "GET").getClass());
    }

    @Test
    public void returnsDirectoryController() {
        MockController retrievedController = (MockController) routes.retrieveDirectoryController();

        assertEquals(directoryController.getBody(), retrievedController.getBody());
    }

    @Test
    public void returnsFileController() {
        MockController retrievedController = (MockController) routes.retrieveFileController();

        assertEquals(fileController.getBody(), retrievedController.getBody());
    }

    @Test
    public void returnsErrorController() {
        MockErrorController retrievedController = (MockErrorController) routes.retrieveErrorController(404);

        assertEquals(notFoundController.getBody(), retrievedController.getBody());
    }
}