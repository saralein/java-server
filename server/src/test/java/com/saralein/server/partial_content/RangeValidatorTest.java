package com.saralein.server.partial_content;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RangeValidatorTest {
    private RangeValidator rangeValidator;

    @Before
    public void setUp() {
        rangeValidator = new RangeValidator();
    }

    @Test
    public void denotesIfStartAndEndFormValidRange() {
        assertTrue(rangeValidator.isValidRange(0, 9, 10));
        assertFalse(rangeValidator.isValidRange(0, 10, 10));
        assertFalse(rangeValidator.isValidRange(8, 5, 10));
        assertFalse(rangeValidator.isValidRange(-1, 9, 10));
    }

    @Test
    public void denotesIfPatternsMatchValidRange() {
        assertTrue(rangeValidator.matchesValidRangeFormat("bytes=0-9"));
        assertTrue(rangeValidator.matchesValidRangeFormat("bytes=2-"));
        assertTrue(rangeValidator.matchesValidRangeFormat("bytes=-6"));
        assertFalse(rangeValidator.matchesValidRangeFormat("bytes=-"));
        assertFalse(rangeValidator.matchesValidRangeFormat("unit=0-9"));
        assertFalse(rangeValidator.matchesValidRangeFormat("bytes0-9"));
    }
}
