package com.saralein.server.request;

import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RequestParserTest {
    private Request rootRequest;
    private Request fileRequest;
    private Request postRequest;
    private RequestParser requestParser;

    @Before
    public void setUp() {
        HashMap<String, String> rootHash = new HashMap<String, String>() {{
            put("method", "GET");
            put("uri", "/");
            put("version", "HTTP/1.1");
        }};

        HashMap<String, String> fileHash = new HashMap<String, String>() {{
            put("method", "GET");
            put("uri", "/public/cheetara.jpg");
            put("version", "HTTP/1.1");
        }};

        HashMap<String, String> postHash = new HashMap<String, String>() {{
            put("method", "POST");
            put("uri", "/form");
            put("version", "HTTP/1.1");
            put("Body", "My=Data&More=Data");
        }};

        rootRequest = new Request(rootHash);
        fileRequest = new Request(fileHash);
        postRequest = new Request(postHash);

        requestParser = new RequestParser();
    }

    @Test
    public void returnsRequestWithCorrectInformation() {
        Request parsedRootRequest = requestParser.parse("GET / HTTP/1.1");

        assertEquals(rootRequest.getMethod(), parsedRootRequest.getMethod());
        assertEquals(rootRequest.getUri(), parsedRootRequest.getUri());

        Request parsedFileRequest = requestParser.parse("GET /public/cheetara.jpg HTTP/1.1");

        assertEquals(fileRequest.getMethod(), parsedFileRequest.getMethod());
        assertEquals(fileRequest.getUri(), parsedFileRequest.getUri());

        Request parsedPostRequest = requestParser.parse("POST /form HTTP/1.1\r\n\r\nBody: My=Data&More=Data");

        assertEquals(postRequest.getMethod(), parsedPostRequest.getMethod());
        assertEquals(postRequest.getUri(), parsedPostRequest.getUri());
        assertEquals(postRequest.getBody(), parsedPostRequest.getBody());
    }
}