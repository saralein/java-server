package com.saralein.server.response;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirectoryResponseTest {
    private byte[] responseArray;
    private DirectoryResponse directoryResponse;
    private SysFileHelper sysFileHelper;

    @Before
    public void setUp() {
        String response = "HTTP/1.1 200 OK\r\n" +
                          "Content-Type: text/html\r\n\r\n" +
                          "<li><a href=/public/cheetara.jpg>cheetara.jpg</a></li>" +
                          "<li><a href=/public/marshmallow.gif>marshmallow.gif</a></li>" +
                          "<li><a href=/public/recipe.txt>recipe.txt</a></li>";

        responseArray = response.getBytes();

        sysFileHelper = new SysFileHelper("public");

        directoryResponse = new DirectoryResponse(sysFileHelper);
    }

    @Test
    public void returnsCorrectResponseForDirectory() {
        assertArrayEquals(responseArray, directoryResponse.createResponse());
    }
}