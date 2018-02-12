package com.saralein.server.request.parser;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class HeaderParserTest {
    private Map<String, String> headers;
    private HeaderParser headerParser;

    @Before
    public void setUp() {
        headers = new HashMap<String, String>() {{
            put("Cookie", "type=stuff");
            put("Range", "bytes=0-9");
        }};

        headerParser = new HeaderParser();
    }

    @Test
    public void parsesHeaderWithNoBody() {
        List<String> rawHeaders = new ArrayList<String>() {{
            add("Cookie: type=stuff");
            add("Range: bytes=0-9");
        }};

        assertEquals(headers, headerParser.parse(rawHeaders));
    }
}
