package com.saralein.server.router;

import com.saralein.server.mocks.MockRedirect;
import org.junit.Test;
import static org.junit.Assert.*;

public class RoutesTest {
    private Routes routes = new Routes().get("/redirect", new MockRedirect());

    @Test
    public void addsRoutesForMethods() {
        Routes updatedRoutes = routes.post("/post", new MockRedirect())
                                     .put("/put", new MockRedirect())
                                     .delete("/delete", new MockRedirect())
                                     .head("/head", new MockRedirect())
                                     .options("/options", new MockRedirect());

        assertTrue(updatedRoutes.matchesRouteAndMethod("/redirect", "GET"));
        assertTrue(updatedRoutes.matchesRouteAndMethod("/post", "POST"));
        assertTrue(updatedRoutes.matchesRouteAndMethod("/put", "PUT"));
        assertTrue(updatedRoutes.matchesRouteAndMethod("/delete", "DELETE"));
        assertTrue(updatedRoutes.matchesRouteAndMethod("/head", "HEAD"));
        assertTrue(updatedRoutes.matchesRouteAndMethod("/options", "OPTIONS"));
    }

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
}