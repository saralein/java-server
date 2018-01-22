package com.saralein.server.router;

import org.junit.Test;
import static org.junit.Assert.*;

public class RouteConfigTest {
    private static final String USERNAME_KEY = "username";
    private static final String USERNAME_VALUE = "admin";

    @Test
    public void addsKeyAndValuesToConfig() {
        RouteConfig routeConfig = new RouteConfig()
                .add(USERNAME_KEY, USERNAME_VALUE);

        assertEquals(USERNAME_VALUE, routeConfig.getValue(USERNAME_KEY));
    }
}
