package com.saralein.server.request.parser;

import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class HeaderParserTest {
    private Map<String, String> header;
    private HeaderParser headerParser;

    @Before
    public void setUp() {
        header = new HashMap<String, String>() {{
            put("Cookie", "type=stuff");
            put("Range", "bytes=0-9");
        }};

        headerParser = new HeaderParser();
    }

    @Test
    public void parsesHeaderWithNoBody() {
        assertEquals(header, headerParser.parse("Cookie: type=stuff\r\nRange: bytes=0-9"));
    }
}
