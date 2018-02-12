package com.saralein.server.request.parser;

import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class RangeParserTest {
    private RangeParser rangeParser;

    @Before
    public void setUp() {
        rangeParser = new RangeParser();
    }

    @Test
    public void returnsEmptyRangeForInvalidRangeFormat() {
        Map<String, Integer> range = new HashMap<>();

        assertEquals(range, rangeParser.parse(""));
        assertEquals(range, rangeParser.parse("-"));
        assertEquals(range, rangeParser.parse("unit="));
        assertEquals(range, rangeParser.parse("unit=-"));
        assertEquals(range, rangeParser.parse("bytes=-"));
        assertEquals(range, rangeParser.parse("bytes=1a-9"));
        assertEquals(range, rangeParser.parse("bytes=1-9b"));
    }

    @Test
    public void returnsRangeWithStartAndEnd() {
        Map<String, Integer> range = new HashMap<>();
        range.put("start", 0);
        range.put("end", 9);

        assertEquals(range, rangeParser.parse("bytes=0-9"));
    }

    @Test
    public void returnsRangeWithStartOnly() {
        Map<String, Integer> range = new HashMap<>();
        range.put("start", 563);

        assertEquals(range, rangeParser.parse("bytes=563-"));
    }

    @Test
    public void returnsRangeWithEndOnly() {
        Map<String, Integer> range = new HashMap<>();
        range.put("end", 13);

        assertEquals(range, rangeParser.parse("bytes=-13"));
    }
}
