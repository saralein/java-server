package com.saralein.server.controller;

import com.saralein.server.Controller.DirectoryController;
import com.saralein.server.request.Request;
import com.saralein.server.response.FileHelper;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import com.saralein.server.response.SysFileHelper;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DirectoryControllerTest {
    private byte[] bodyArray;
    private Response directoryResponse;

    @Before
    public void setUp() {
        String body = "<li><a href=/cake.pdf>cake.pdf</a></li>" +
                      "<li><a href=/cheetara.jpg>cheetara.jpg</a></li>" +
                      "<li><a href=/marshmallow.gif>marshmallow.gif</a></li>" +
                      "<li><a href=/recipe.txt>recipe.txt</a></li>" +
                      "<li><a href=/sloths/>sloths/</a></li>";

        bodyArray = body.getBytes();

        String rootPath = System.getProperty("user.dir") + "/" + "public";
        Path root = Paths.get(rootPath);
        FileHelper sysFileHelper = new SysFileHelper(root);
        Request request = new Request(new HashMap<String, String>() {{
            put("method", "GET");
            put("uri", "/");
            put("version", "HTTP/1.1");
        }});

        DirectoryController directoryController = new DirectoryController(sysFileHelper);
        directoryResponse = directoryController.createResponse(request);
    }

    @Test
    public void returnsResponseWithCorrectHeader() {
        Header header = directoryResponse.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
    }

    @Test
    public void returnsResponseWithCorrectBody() {
        assertArrayEquals(bodyArray, directoryResponse.getBody());
    }
}