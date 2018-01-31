package com.saralein.cobspec.data;

import com.saralein.server.cookies.Cookie;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class CookieStoreTest {
    private final List<Cookie> TWO_COOKIE_LIST = new ArrayList<Cookie>() {{
        add(new Cookie("type", "chocolate"));
        add(new Cookie("baker", "Phil"));
    }};
    private CookieStore cookieStore;

    @Before
    public void setUp() {
        cookieStore = new CookieStore();
    }

    private List<String> mapToString(List<Cookie> cookies) {
        return cookies.stream()
                .map(Cookie::toString)
                .collect(Collectors.toList());
    }

    private boolean haveMatchingToString(List<Cookie> expected, List<Cookie> actual) {
        List<String> expectedStrings = mapToString(expected);
        List<String> actualStrings = mapToString(actual);

        return expectedStrings.equals(actualStrings);
    }

    @Test
    public void addsCookiesToList() {
        List<Cookie> cookies = cookieStore.getCookies();

        assertTrue(cookies.isEmpty());

        cookieStore.addCookies(TWO_COOKIE_LIST);
        List<Cookie> modifiedCookies = cookieStore.getCookies();

        assertEquals(TWO_COOKIE_LIST.size(), modifiedCookies.size());
        assertTrue(haveMatchingToString(TWO_COOKIE_LIST, modifiedCookies));
    }

    @Test
    public void checksIfStoreContainsCookie() {
        cookieStore.addCookies(TWO_COOKIE_LIST);
        Cookie chocolate = new Cookie("type", "chocolate");
        Cookie dozen = new Cookie("amount", "12");

        assertTrue(cookieStore.containsCookie(chocolate));
        assertFalse(cookieStore.containsCookie(dozen));
    }
}
