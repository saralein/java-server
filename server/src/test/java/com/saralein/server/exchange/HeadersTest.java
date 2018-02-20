package com.saralein.server.exchange;

import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class HeadersTest {
    @Test
    public void addsAndRetrievesHeaders() {
        Map<String, String> expected = new HashMap<>();
        expected.put("Content-Type", "text/html");
        Headers headers = new Headers();
        headers.addHeader("Content-Type", "text/html");

        assertEquals(expected, headers.getHeaders());

        expected.put("Content-Length", "12");
        headers.addHeader("Content-Length", "12");

        assertEquals(expected, headers.getHeaders());
    }

    @Test
    public void comparesHeaderEquality() {
        Headers headers = new Headers();
        headers.addHeader("Content-Type", "text/plain");
        headers.addHeader("Content-Length", "12");

        Headers matching = new Headers();
        matching.addHeader("Content-Type", "text/plain");
        matching.addHeader("Content-Length", "12");

        assert (headers.equals(matching) && matching.equals(headers));
        assert (headers.hashCode() == matching.hashCode());
    }
}
