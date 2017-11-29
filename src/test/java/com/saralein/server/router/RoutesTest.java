package com.saralein.server.router;

import com.saralein.server.Controller.RedirectController;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoutesTest {
    private Routes routes = new SetupRoutes().setup();

    @Test
    public void checksIfRouteExists() {
        assertFalse(routes.isRoute("/doggos"));
        assertTrue(routes.isRoute("/redirect"));
    }

    @Test
    public void addsRouteToRoutes() {
       routes.addRoute("/doggos", new RedirectController());

       assertTrue(routes.isRoute("/doggos"));
    }

    @Test
    public void returnsControllerForRoute() {
        assertEquals(RedirectController.class, routes.getController("/redirect").getClass());
    }
}