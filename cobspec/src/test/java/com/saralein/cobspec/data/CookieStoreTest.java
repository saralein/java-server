package com.saralein.cobspec.data;

import com.saralein.server.exchange.Cookie;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class CookieStoreTest {
    private List<Cookie> twoCookies;
    private CookieStore cookieStore;

    private boolean cookiesAreEqual(List<Cookie> expected, List<Cookie> actual) {
        expected.sort(Cookie::compareTo);
        actual.sort(Cookie::compareTo);
        return expected.equals(actual);
    }

    @Before
    public void setUp() {
        cookieStore = new CookieStore();
        twoCookies = new ArrayList<Cookie>() {{
            add(new Cookie("type", "chocolate"));
            add(new Cookie("baker", "Phil"));
        }};
    }

    @Test
    public void addsCookiesToList() {
        List<Cookie> cookies = cookieStore.getCookies();

        assertTrue(cookies.isEmpty());

        cookieStore.addCookies(twoCookies);
        List<Cookie> modifiedCookies = cookieStore.getCookies();

        assertEquals(twoCookies.size(), modifiedCookies.size());
        assertTrue(cookiesAreEqual(twoCookies, modifiedCookies));
    }

    @Test
    public void checksIfStoreContainsCookie() {
        cookieStore.addCookies(twoCookies);
        Cookie chocolate = new Cookie("type", "chocolate");
        Cookie dozen = new Cookie("amount", "12");

        assertTrue(cookieStore.containsCookie(chocolate));
        assertFalse(cookieStore.containsCookie(dozen));
    }
}
