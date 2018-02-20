package com.saralein.server.exchange;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CookieTest {
    private String name;
    private String value;
    private Cookie cookie;

    @Before
    public void setUp() {
        name = "type";
        value = "chocolate";
        cookie = new Cookie(name, value);
    }

    @Test
    public void getsCookieName() {
        assertEquals(name, cookie.getName());
    }

    @Test
    public void getsCookieValue() {
        assertEquals(value, cookie.getValue());
    }

    @Test
    public void comparesCookieEquality() {
        Cookie cookie = new Cookie("type", "chocolate");
        Cookie matching = new Cookie("type", "chocolate");
        Cookie nonMatching = new Cookie("baker", "Phil");

        assert (cookie.equals(matching));
        assertFalse(cookie.equals(nonMatching));
    }

    @Test
    public void comparesCookiesWithCompareTo() {
        Cookie cookie = new Cookie("type", "chocolate");
        Cookie matching = new Cookie("type", "chocolate");
        Cookie before = new Cookie("baker", "Phil");
        Cookie after = new Cookie("zoo", "Brookfield");

        assert (cookie.compareTo(matching) == 0);
        assert (cookie.compareTo(before) > 0);
        assert (cookie.compareTo(after) < 0);
    }
}
