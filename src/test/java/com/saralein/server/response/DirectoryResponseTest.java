package com.saralein.server.response;

import java.io.File;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DirectoryResponseTest {
    private byte[] responseArray;
    private DirectoryResponse directoryResponse;

    @Before
    public void setUp() {
        String response = "HTTP/1.1 200 OK\r\n" +
                          "Content-Type: text/html\r\n\r\n" +
                          "<li><a href=cake.pdf>cake.pdf</a></li>" +
                          "<li><a href=cheetara.jpg>cheetara.jpg</a></li>" +
                          "<li><a href=marshmallow.gif>marshmallow.gif</a></li>" +
                          "<li><a href=recipe.txt>recipe.txt</a></li>";

        responseArray = response.getBytes();

        File directoryFile = new File("public");
        FileHelper sysFileHelper = new SysFileHelper("public");

        directoryResponse = new DirectoryResponse(directoryFile, sysFileHelper);
    }

    @Test
    public void returnsCorrectResponseForDirectory() {
        assertArrayEquals(responseArray, directoryResponse.createResponse());
    }
}