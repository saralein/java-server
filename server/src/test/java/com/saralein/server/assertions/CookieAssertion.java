package com.saralein.server.assertions;

import com.saralein.server.exchange.Cookie;
import java.util.HashSet;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class CookieAssertion {
    public static void assertCookiesAreEqual(List<Cookie> expected, List<Cookie> actual) {
        assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }
}
