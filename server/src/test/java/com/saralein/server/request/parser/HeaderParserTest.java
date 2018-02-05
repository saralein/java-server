package com.saralein.server.request.parser;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HeaderParserTest {
    private List<String> request;
    private Map<String, String> header;
    private HeaderParser headerParser;

    @Before
    public void setUp() {
        request = new ArrayList<String>() {{
            add("Request line");
            add("Cookie: type=stuff");
            add("Range: bytes=0-9");
        }};

        header = new HashMap<String, String>() {{
            put("Cookie", "type=stuff");
            put("Range", "bytes=0-9");
        }};

        headerParser = new HeaderParser();
    }

    @Test
    public void parsesHeaderWithNoBody() {
        assertEquals(header, headerParser.parse(request));
    }

    @Test
    public void parsesHeaderWithBody() {
        request.add("Content-Length: 4");
        request.add("body");
        header.put("Content-Length", "4");

        assertEquals(header, headerParser.parse(request));
    }
}
