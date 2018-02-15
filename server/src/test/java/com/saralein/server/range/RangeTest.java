package com.saralein.server.range;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RangeTest {
    private Range range;

    @Before
    public void setUp() {
        range = new Range(1, 5);
    }

    @Test
    public void getsStartingPointOfRange() {
        assertEquals(1, range.getStart());
    }

    @Test
    public void getsEndPointOfRange() {
        assertEquals(5, range.getEnd());
    }
}
