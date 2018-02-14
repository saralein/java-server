package com.saralein.server.protocol;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MethodsTest {
    @Test
    public void returnsStringOfAllMethods() {
        assertEquals("GET,OPTIONS,HEAD,POST,PUT", Methods.allowAllButDeleteAndPatch());
    }

    @Test
    public void returnsStringOfGetAndOptions() {
        assertEquals("GET,OPTIONS", Methods.allowGetAndOptions());
    }

    @Test
    public void returnsStringOfGetHeadAndPatch() {
        assertEquals("GET,HEAD", Methods.allowedFileMethods());
    }
}
