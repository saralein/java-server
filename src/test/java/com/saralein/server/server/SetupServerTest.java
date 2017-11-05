package com.saralein.server.server;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SetupServerTest {
    String[] args;

    @Before
    public void setUp() {
        args = new String[]{"1337"};
    }

    @Test
    public void setsUpAndReturnsNewServer() {
        Server server = SetupServer.setup(args);

        assertNotNull(server);
        assertEquals(Server.class, server.getClass());
    }

}