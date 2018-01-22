package com.saralein.server.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import com.saralein.server.FileHelper;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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

        String rootPath = System.getProperty("user.dir") + "/src/test/public";
        Path root = Paths.get(rootPath);
        FileHelper fileHelper = new FileHelper(root);
        Request request = new Request.Builder()
                .method("GET")
                .uri("/")
                .build();

        DirectoryController directoryController = new DirectoryController(fileHelper);
        directoryResponse = directoryController.respond(request);
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
