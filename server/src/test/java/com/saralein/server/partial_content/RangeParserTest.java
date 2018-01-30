package com.saralein.server.partial_content;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class RangeParserTest {
    private final String range;
    private final int start;
    private final int end;
    private RangeParser rangeParser;

    public RangeParserTest(String range, int start, int end) {
        this.range = range;
        this.start = start;
        this.end = end;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {"bytes=0-9", 0, 9},
                {"bytes=2-6", 2, 6},
                {"bytes=2-", 2, 9},
                {"bytes=-6", 4, 9}
        });
    }

    @Before
    public void setUp() {
        rangeParser = new RangeParser();
    }

    @Test
    public void parsesPossibleRangeFormats() {
        Range parsed = rangeParser.parse(range, 10);

        assertEquals(start, parsed.getStart());
        assertEquals(end, parsed.getEnd());
    }
}
