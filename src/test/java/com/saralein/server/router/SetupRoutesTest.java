package com.saralein.server.router;

import com.saralein.server.Controller.RedirectController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SetupRoutesTest {
    SetupRoutes setupRoutes;

    @Before
    public void setUp() {
       setupRoutes = new SetupRoutes();
    }

    @Test
    public void setUpAndReturnsNewRoutes() {
        Routes routes = setupRoutes.setup();

        assertTrue(routes.isRoute("/redirect"));
        assertEquals(RedirectController.class, routes.getController("/redirect").getClass());
    }
}