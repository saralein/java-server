package com.saralein.server.cookies;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CookieTest {
    private final String NAME = "type";
    private final String VALUE = "chocolate";
    private Cookie cookie;

    @Before
    public void setUp() {
        cookie = new Cookie(NAME, VALUE);
    }

    @Test
    public void getsCookieName() {
        assertEquals(NAME, cookie.getName());
    }

    @Test
    public void getsCookieValue() {
        assertEquals(VALUE, cookie.getValue());
    }

    @Test
    public void formatsNameAndValueToString() {
        assertEquals(NAME + "=" + VALUE, cookie.toString());
    }
}
