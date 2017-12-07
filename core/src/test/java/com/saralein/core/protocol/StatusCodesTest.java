package com.saralein.core.protocol;

import org.junit.Test;
import static org.junit.Assert.*;

public class StatusCodesTest {
    @Test
    public void getsFullStatusBasedOnCode() {
        assertEquals("200 OK", StatusCodes.retrieve(200));
        assertEquals("302 Found", StatusCodes.retrieve(302));
        assertEquals("404 Not Found", StatusCodes.retrieve(404));
    }
}