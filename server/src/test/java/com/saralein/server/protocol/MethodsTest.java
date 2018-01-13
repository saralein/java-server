package com.saralein.server.protocol;

import static org.junit.Assert.*;
import org.junit.Test;

public class MethodsTest {
    @Test
    public void returnsStringOfAllMethods() {
        assertEquals("GET,OPTIONS,HEAD,POST,PUT", Methods.allowNonDestructiveMethods());
    }

    @Test
    public void returnsStringOfGetAndOptions() {
        assertEquals("GET,OPTIONS", Methods.allowGetAndOptions());
    }

    @Test
    public void returnsStringOfGetAndHead() {
        assertEquals("GET,HEAD", Methods.allowedFileSystemMethods());
    }
}