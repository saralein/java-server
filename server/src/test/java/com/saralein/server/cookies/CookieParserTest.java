package com.saralein.server.cookies;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CookieParserTest {
    private final List<Cookie> EMPTY_LIST = new ArrayList<>();
    private final List<Cookie> ONE_COOKIE_LIST = new ArrayList<Cookie>() {{
        add(new Cookie("type", "chocolate"));
    }};
    private final List<Cookie> TWO_COOKIE_LIST = new ArrayList<Cookie>() {{
        add(new Cookie("type", "chocolate"));
        add(new Cookie("baker", "Phil"));
    }};
    private CookieParser cookieParser;

    @Before
    public void setUp() {
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
    public void returnsEmptyListForInvalidCookieParametersInUrl() {
        assertEquals(EMPTY_LIST, cookieParser.parseFromUri("/cookies"));
        assertEquals(EMPTY_LIST, cookieParser.parseFromUri("/cookies?"));
        assertEquals(EMPTY_LIST, cookieParser.parseFromUri("/cookie?type"));
        assertEquals(EMPTY_LIST, cookieParser.parseFromUri("/cookie?type="));
    }

    @Test
    public void returnsCookieListForSingleCookieInUrl() {
        List<Cookie> parsedCookies = cookieParser.parseFromUri("/cookie?type=chocolate");
        assertEquals(ONE_COOKIE_LIST.size(), parsedCookies.size());
        assertTrue(haveMatchingToString(ONE_COOKIE_LIST, parsedCookies));
    }

    @Test
    public void returnsCookieListForMultipleCookiesInUrl() {
        List<Cookie> parsedCookies = cookieParser.parseFromUri("/cookie?type=chocolate&baker=Phil");
        assertEquals(TWO_COOKIE_LIST.size(), parsedCookies.size());
        assertTrue(haveMatchingToString(TWO_COOKIE_LIST, parsedCookies));
    }

    @Test
    public void returnsEmptyListForInvalidCookieHeader() {
        assertEquals(EMPTY_LIST, cookieParser.parseFromHeader(""));
        assertEquals(EMPTY_LIST, cookieParser.parseFromHeader(";"));
    }

    @Test
    public void returnsCookieListForSingleCookieHeader() {
        List<Cookie> parsedCookies = cookieParser.parseFromHeader("type=chocolate");
        assertEquals(ONE_COOKIE_LIST.size(), parsedCookies.size());
        assertTrue(haveMatchingToString(ONE_COOKIE_LIST, parsedCookies));
    }

    @Test
    public void returnsCookieListForMultipleCookieHeader() {
        List<Cookie> parsedCookies = cookieParser.parseFromHeader("type=chocolate;baker=Phil;");
        assertEquals(TWO_COOKIE_LIST.size(), parsedCookies.size());
        assertTrue(haveMatchingToString(TWO_COOKIE_LIST, parsedCookies));
    }
}
