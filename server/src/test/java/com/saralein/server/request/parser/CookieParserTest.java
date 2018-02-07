package com.saralein.server.request.parser;

import com.saralein.server.exchange.Cookie;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CookieParserTest {
    private List<Cookie> emptyCookies;
    private List<Cookie> oneCookie;
    private List<Cookie> twoCookies;
    private CookieParser cookieParser;

    @Before
    public void setUp() {
        Cookie chocolateCookie = new Cookie("type", "chocolate");
        Cookie bakerCookie = new Cookie("baker", "Phil");
        emptyCookies = new ArrayList<>();
        oneCookie = new ArrayList<Cookie>() {{
            add(chocolateCookie);
        }};
        twoCookies = new ArrayList<Cookie>() {{
            add(chocolateCookie);
            add(bakerCookie);
        }};
        cookieParser = new CookieParser();
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
    public void returnsEmptyListForInvalidCookieHeader() {
        assertEquals(emptyCookies, cookieParser.parse(""));
        assertEquals(emptyCookies, cookieParser.parse(";"));
    }

    @Test
    public void returnsCookieListForSingleCookieHeader() {
        List<Cookie> parsedCookies = cookieParser.parse("type=chocolate");
        assertEquals(oneCookie.size(), parsedCookies.size());
        assertTrue(haveMatchingToString(oneCookie, parsedCookies));
    }

    @Test
    public void returnsCookieListForMultipleCookieHeader() {
        List<Cookie> parsedCookies = cookieParser.parse("type=chocolate;baker=Phil;");
        assertEquals(twoCookies.size(), parsedCookies.size());
        assertTrue(haveMatchingToString(twoCookies, parsedCookies));
    }
}
