package com.saralein.core.router;

import com.saralein.core.controller.*;
import com.saralein.core.mocks.MockController;
import com.saralein.core.mocks.MockRedirect;
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class RoutesTest {
    private MockController directoryController = new MockController(200, "Directory response");
    private MockController fileController = new MockController(200, "File response");
    private MockController notFoundController = new MockController(404, "Not found response");

    private HashMap<String, Controller> redirect = new HashMap<String, Controller>(){{
        put("GET", new MockRedirect());
    }};
    private HashMap<String, HashMap<String, Controller>> customRoutes =
            new HashMap<String, HashMap<String, Controller>>() {{
                put("/redirect", redirect);
            }};
    private Routes routes = new Routes(customRoutes, directoryController, fileController, notFoundController);

    @Test
    public void checksIfRouteExists() {
        assertFalse(routes.isRoute("/doggos", "GET"));
        assertTrue(routes.isRoute("/redirect", "GET"));
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
    public void returns404Controller() {
        MockController retrievedController = (MockController) routes.retrieve404Controller();

        assertEquals(notFoundController.getBody(), retrievedController.getBody());
    }
}