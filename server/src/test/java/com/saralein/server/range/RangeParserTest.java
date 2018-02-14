package com.saralein.server.range;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertEquals;

public class RangeParserTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    private RangeParser rangeParser;

    @Before
    public void setUp() {
        rangeParser = new RangeParser();
    }

    @Test
    public void parsesRangeWithStartAndEnd() throws Exception {
        Range parsed = rangeParser.parse("bytes=2-6", 10);

        assertEquals(2, parsed.getStart());
        assertEquals(6, parsed.getEnd());
    }

    @Test
    public void parsesRangeWithStartOnly() throws Exception {
        Range parsed = rangeParser.parse("bytes=2-", 10);

        assertEquals(2, parsed.getStart());
        assertEquals(9, parsed.getEnd());
    }

    @Test
    public void parsesRangeWithEndOnly() throws Exception {
        Range parsed = rangeParser.parse("bytes=-6", 10);

        assertEquals(4, parsed.getStart());
        assertEquals(9, parsed.getEnd());
    }

    @Test(expected = Exception.class)
    public void throwsForInvalidRangeFormat() throws Exception {
        rangeParser.parse("bytes=s-2", 10);
    }

    @Test(expected = Exception.class)
    public void throwsIfOutOfRange() throws Exception {
        rangeParser.parse("bytes=-11", 10);
    }
}
